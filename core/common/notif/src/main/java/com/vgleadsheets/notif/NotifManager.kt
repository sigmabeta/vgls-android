package com.vgleadsheets.notif

import com.squareup.moshi.JsonAdapter
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class NotifManager(
    private val storage: Storage,
    private val jsonAdapter: JsonAdapter<NotifState>,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) {
    private val internalNotifState = MutableStateFlow(NotifState())
    val notifState = internalNotifState.asStateFlow()

    init {
        setupNotifStorageCollection()
    }

    fun addNotif(notif: Notif) {
        updateNotifsInStorage { notifs ->
            notifs[notif.id] = notif
        }
    }

    fun removeNotif(id: Long) {
        updateNotifsInStorage { notifs ->
            notifs.remove(id)
        }
    }

    private fun updateNotifsInStorage(updater: (MutableMap<Long, Notif>) -> Unit) {
        val oldNotifState = internalNotifState.value
        val newNotifsMap = oldNotifState.notifs.toMutableMap()

        updater(newNotifsMap)

        val newNotifsState = oldNotifState.copy(notifs = newNotifsMap)

        storage.saveString(
            key = KEY_NOTIF_STATE,
            value = newNotifsState.toJson(),
        )
    }

    private fun setupNotifStorageCollection() {
        storage.savedStringFlow(KEY_NOTIF_STATE)
            .map { it?.toObject() }
            .filterNotNull()
            .onEach { newState ->
                internalNotifState.update {
                    newState
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun NotifState.toJson() = jsonAdapter.toJson(this)

    @Suppress("PrintStackTrace", "TooGenericExceptionCaught")
    private fun String?.toObject(): NotifState? {
        if (this == null) {
            hatchet.e("No saved Notif information.")
            return null
        }
        return try {
            val fromJson = jsonAdapter.fromJson(this)

            if (fromJson == null) {
                hatchet.e("Invalid Notif information: \"$this\"")
            }

            fromJson
        } catch (ex: Exception) {
            hatchet.e("Error reading Notifs from storage: ${ex.message}")
            ex.printStackTrace()
            null
        }
    }

    companion object {
        const val DEP_NAME_JSON_ADAPTER_NOTIF = "NotifJsonAdapter"

        const val KEY_NOTIF_STATE = "NotifManager.NotifState"
    }
}
