package com.vgleadsheets.repository

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.di.ActionDeserializer
import net.sigmabeta.sage.connectivity.NetworkStatus
import net.sigmabeta.sage.connectivity.VglsNetworkUnavailableException
import net.sigmabeta.sage.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.notif.Notif
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class UpdateManager(
    private val vglsApi: VglsApi,
    private val dbUpdater: DbUpdater,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
    private val threeTen: ThreeTenTime,
    private val actionDeserializer: ActionDeserializer,
    private val hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val notifManager: NotifManager,
    private val stringProvider: StringProvider,
) {
    init {
        setupApiUpdateTimeCheckFlow()
    }

    @Suppress("TooGenericExceptionCaught")
    fun refresh() {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Launching refresh attempt...")
            val lastAppCheckTime = Time(TimeType.LAST_APP_CHECK.ordinal, 0)
            dbStatisticsDataSource.insert(lastAppCheckTime)
        }
    }

    fun getLastApiUpdateTime(): Flow<Time> = getLastApiUpdateTimeInternal()

    private fun setupApiUpdateTimeCheckFlow() {
        getLastCheckTime()
            .map { checkLastUpdateTimeIsOldEnough(it) }
            .filter { it }
            .onEach { refreshLastApiUpdateTime() }
            .flatMapLatest { dbUpdateTimeCheckFlow() }
            .filter { it }
            .flatMapLatest { refreshInternal() }
            .filter { it }
            .onEach { onUpdateSuccess() }
            .onCompletion { hatchet.e("Cancelled update flow because of ${it?.message}") }
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

    @Suppress("ReturnCount")
    suspend fun refreshAndAwait(): Boolean {
        if (!refreshLastApiUpdateTime()) return false
        val needsUpdate = dbUpdateTimeCheckFlow().first()
        if (!needsUpdate) return true
        return refreshInternal().first()
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun refreshLastApiUpdateTime(): Boolean {
        hatchet.i("Requesting API update time check...")

        val lastUpdate = try {
            vglsApi.getLastUpdateTime()
        } catch (ex: Exception) {
            emitApiUpdateErrors(ex)
            return false
        }

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
        return true
    }

    private fun dbUpdateTimeCheckFlow() = combine(
        getLastDbUpdateTimeInternal(),
        getLastApiUpdateTimeInternal(),
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

    private fun refreshInternal(): Flow<Boolean> {
        hatchet.i("Requesting DB update...")
        return dbUpdater.refresh()
            .catch { ex ->
                emitDbUpdateErrors(ex)
                emit(false)
            }
    }

    private fun emitDbUpdateErrors(ex: Throwable) {
        hatchet.e("DB update failed: ${ex.message}")
        ex.printStackTrace()
        onUpdateFailed(describeFailure(StringId.ERROR_UPDATE_DB_PREFIX, ex, StringId.ERROR_UPDATE_DB_REASON_GENERIC))
    }

    private fun emitApiUpdateErrors(ex: Throwable) {
        hatchet.e("Last update API call failed: ${ex.message}")
        ex.printStackTrace()
        onLastUpdateCheckFailed(
            describeFailure(
                StringId.ERROR_UPDATE_API_PREFIX,
                ex,
                StringId.ERROR_UPDATE_REASON_GENERIC
            )
        )
    }

    private fun describeFailure(prefixId: StringId, ex: Throwable, genericFallbackId: StringId): String {
        val networkStatus = (ex as? VglsNetworkUnavailableException)?.networkStatus
        val reason = when (networkStatus) {
            NetworkStatus.OFFLINE ->
                stringProvider.getString(StringId.ERROR_UPDATE_REASON_OFFLINE)

            NetworkStatus.ONLINE_NO_INTERNET ->
                stringProvider.getString(StringId.ERROR_UPDATE_REASON_NO_INTERNET)

            NetworkStatus.ONLINE_API_UNREACHABLE ->
                stringProvider.getString(StringId.ERROR_UPDATE_REASON_API_UNREACHABLE)

            NetworkStatus.ONLINE, null ->
                stringProvider.getString(genericFallbackId)
        }
        return stringProvider.getStringTwoArgs(
            StringId.ERROR_UPDATE_DESCRIPTION_FORMAT,
            stringProvider.getString(prefixId),
            reason,
        )
    }

    private fun onUpdateSuccess() {
        val title = StringId.NOTIF_TITLE_DB_UPDATE_SUCCESS
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = "VGLS has been updated and there are new sheets ready to play!",
                actionLabel = "See what's new",
                category = NotifCategory.VGLS_UPDATE,
                isOneTime = true,
                action = actionDeserializer.serializeAction(VglsAction.DbSeeWhatsNewClicked),
            )
        )
    }

    private fun onLastUpdateCheckFailed(description: String) {
        val title = StringId.ERROR_API_UPDATE
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = description,
                actionLabel = "Try again",
                category = NotifCategory.ERROR,
                isOneTime = true,
                action = actionDeserializer.serializeAction(VglsAction.RefreshDbClicked),
            )
        )
    }

    private fun onUpdateFailed(description: String) {
        val title = StringId.ERROR_DB_UPDATE
        notifManager.addNotif(
            Notif(
                id = title.hashCode().toLong(),
                title = title,
                description = description,
                actionLabel = "Try again",
                category = NotifCategory.ERROR,
                isOneTime = true,
                action = actionDeserializer.serializeAction(VglsAction.RefreshDbClicked),
            )
        )
    }

    private fun getLastCheckTime() = dbStatisticsDataSource
        .getTime(TimeType.LAST_APP_CHECK.ordinal)

    private fun getLastDbUpdateTimeInternal() = dbStatisticsDataSource
        .getTime(TimeType.LAST_DB_UPDATE.ordinal)

    private fun getLastApiUpdateTimeInternal() = dbStatisticsDataSource
        .getTime(TimeType.LAST_VGLS_UPDATE.ordinal)

    companion object {
        val AGE_THRESHOLD = 4.toDuration(DurationUnit.HOURS)
    }
}
