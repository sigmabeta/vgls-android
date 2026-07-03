package com.vgleadsheets.notif

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.storage.common.Storage

class NotifManager(
    private val storage: Storage,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: SageDispatchers,
    private val hatchet: Hatchet,
) {
    // The single multiplatform JSON codec for the persisted NotifState (replaces the injected Moshi
    // adapter). ignoreUnknownKeys keeps old persisted state readable if the schema gains fields.
    private val json = Json { ignoreUnknownKeys = true }

    private val internalNotifState = MutableStateFlow(NotifState())
    val notifState = internalNotifState.asStateFlow()

    init {
        setupNotifStorageCollection()
        removePreviousOneTimeNotifs()
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
            .map { it?.toNotifState() }
            .filterNotNull()
            .onEach { newState ->
                internalNotifState.update {
                    newState
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun removePreviousOneTimeNotifs() {
        internalNotifState
            .take(1)
            .onEach {
                hatchet.v("Clearing previous one-time notifs...")
                updateNotifsInStorage { notifs ->
                    notifs.filterNot { entry -> entry.value.isOneTime }
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun NotifState.toJson() = json.encodeToString(this)

    @Suppress("PrintStackTrace", "TooGenericExceptionCaught", "ReturnCount")
    private fun String?.toNotifState(): NotifState? {
        if (this == null) {
            hatchet.e("No saved Notif information.")
            return null
        }
        return try {
            json.decodeFromString<NotifState>(this)
        } catch (ex: Exception) {
            hatchet.e("Error reading Notifs from storage: ${ex.message}")
            ex.printStackTrace()
            null
        }
    }

    companion object {
        const val KEY_NOTIF_STATE = "NotifManager.NotifState"
    }
}
