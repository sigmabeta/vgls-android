package com.vgleadsheets.repository

import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.notif.Notif
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.RealRepository.Companion.AGE_THRESHOLD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class UpdateManager(
    private val vglsApi: VglsApi,
    private val dbUpdater: DbUpdater,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
    private val threeTen: ThreeTenTime,
    private val eventDispatcher: EventDispatcher,
    private val hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val notifManager: NotifManager
) {
    init {
        setupApiUpdateTimeCheckFlow()
    }

    fun refresh() {
        coroutineScope.launch(dispatchers.disk) {
            val lastAppCheckTime = Time(TimeType.LAST_APP_CHECK.ordinal, 0)
            dbStatisticsDataSource.insert(lastAppCheckTime)
        }
    }

    fun getLastDbUpdateTime(): Flow<Time> = getLastDbUpdateTimeInternal()

    fun getLastApiUpdateTime(): Flow<Time> = getLastApiUpdateTimeInternal()

    private fun setupApiUpdateTimeCheckFlow() {
        getLastCheckTime()
            .map { checkLastUpdateTimeIsOldEnough(it) }
            .catch { emitDbUpdateErrors(it) }
            .filter { it }
            .onEach { refreshLastApiUpdateTime() }
            .catch { emitApiUpdateErrors(it) }
            .flatMapLatest { dbUpdateTimeCheckFlow() }
            .catch { emitDbUpdateErrors(it) }
            .filter { it }
            .flatMapLatest { refreshInternal() }
            .catch { emitDbUpdateErrors(it) }
            .onEach { onUpdateSuccess() }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun checkLastUpdateTimeIsOldEnough(lastCheckTime: Time): Boolean {
        if (lastCheckTime.timeMs == 0L) {
            hatchet.i("Forcing API refresh.")
            return true
        }

        val currentTime = threeTen.now().toInstant().toEpochMilli()
        val lastCheckAgeMillis = currentTime - lastCheckTime.timeMs
        val lastCheckAge = lastCheckAgeMillis.toDuration(DurationUnit.MILLISECONDS)

        hatchet.d("Last time we checked VGLS for updates was $lastCheckAge ago.")

        return lastCheckAge > AGE_THRESHOLD
    }

    private suspend fun refreshLastApiUpdateTime() {
        hatchet.i("Requesting API update time check...")

        val lastUpdate = vglsApi.getLastUpdateTime()
        val lastUpdateInstant = Instant.parse(lastUpdate.last_updated)
        val lastUpdateTime = Time(
            TimeType.LAST_VGLS_UPDATE.ordinal,
            lastUpdateInstant.toEpochMilli()
        )

        val lastAppCheckTime = Time(
            TimeType.LAST_APP_CHECK.ordinal,
            threeTen.now().toInstant().toEpochMilli()
        )

        hatchet.d("VGLS was last updated at $lastUpdateInstant")

        dbStatisticsDataSource.insert(lastUpdateTime)
        dbStatisticsDataSource.insert(lastAppCheckTime)
    }

    private fun dbUpdateTimeCheckFlow() = combine(
        getLastDbUpdateTimeInternal().take(1),
        getLastApiUpdateTimeInternal().take(1)
    ) { lastDbUpdateTime, lastApiUpdateTime ->
        if (lastDbUpdateTime.timeMs == 0L) {
            hatchet.i("DB has never been updated.")
            return@combine true
        }
        val updateDiffMillis = lastApiUpdateTime.timeMs - lastDbUpdateTime.timeMs
        val updateDiff = updateDiffMillis.toDuration(DurationUnit.MILLISECONDS)

        if (updateDiffMillis <= 0) {
            hatchet.d("DB data is ${-updateDiff} newer than server data.")
            return@combine false
        }
        hatchet.d("DB data is $updateDiff older than server data.")
        true
    }

    private fun refreshInternal(): Flow<Unit> {
        hatchet.i("Requesting DB update...")
        return dbUpdater.refresh()
    }

    private fun emitDbUpdateErrors(ex: Throwable) {
        hatchet.e("DB update failed: ${ex.message}")
        ex.printStackTrace()
        onUpdateFailed(ex)
    }

    private fun emitApiUpdateErrors(ex: Throwable) {
        hatchet.e("Last update API call failed: ${ex.message}")
        ex.printStackTrace()
        onLastUpdateCheckFailed(ex)
    }

    private fun onUpdateSuccess() {
        val title = "New Sheets Available"
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = "VGLS has been updated and there are new sheets ready to play!",
                actionLabel = "See what's new",
                category = NotifCategory.VGLS_UPDATE,
                isOneTime = true,
                action = VglsAction.SeeWhatsNewClicked,
            )
        )
    }

    private fun onLastUpdateCheckFailed(ex: Throwable) {
        val title = "Last Update Check Failed"
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = "Error on last update API call:\n\n ${ex.message?: "No exception details available."}",
                actionLabel = "Try again",
                category = NotifCategory.ERROR,
                isOneTime = true,
                action = VglsAction.RefreshDbClicked(),
            )
        )
    }

    private fun onUpdateFailed(ex: Throwable) {
        val title = "Update Failed"
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = "Error on DB update:\n\n${ex.message ?: "No exception details available."}",
                actionLabel = "Try again",
                category = NotifCategory.ERROR,
                isOneTime = true,
                action = VglsAction.RefreshDbClicked(),
            )
        )
    }

    private fun getLastCheckTime() = dbStatisticsDataSource
        .getTime(TimeType.LAST_APP_CHECK.ordinal)

    private fun getLastDbUpdateTimeInternal() = dbStatisticsDataSource
        .getTime(TimeType.LAST_DB_UPDATE.ordinal)

    private fun getLastApiUpdateTimeInternal() = dbStatisticsDataSource
        .getTime(TimeType.LAST_VGLS_UPDATE.ordinal)
}
