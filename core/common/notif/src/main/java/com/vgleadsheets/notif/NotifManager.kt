package com.vgleadsheets.notif

import com.squareup.moshi.JsonAdapter
import com.vgleadsheets.appcomm.di.ActionDeserializer
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
    private val notifStateJsonAdapter: JsonAdapter<NotifState>,
    private val actionDeserializer: ActionDeserializer,
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
            val actualNotif = if (notif.action != null) {
                val genericAction = actionDeserializer.serializeAction(notif.action)

                notif.copy(
                    action = null,
                    genericAction = genericAction,
                )
            } else {
                notif
            }
            notifs[notif.id] = actualNotif
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

    private fun NotifState.toJson() = notifStateJsonAdapter.toJson(this)

    @Suppress("PrintStackTrace", "TooGenericExceptionCaught", "ReturnCount")
    private fun String?.toNotifState(): NotifState? {
        if (this == null) {
            hatchet.e("No saved Notif information.")
            return null
        }
        return try {
            val fromJson = notifStateJsonAdapter.fromJson(this)

            if (fromJson == null) {
                hatchet.e("Invalid Notif information: \"$this\"")
                return null
            }

            val notifs = fromJson.notifs
                .map { entry ->
                    val actionLessNotif = entry.value

                    hatchet.v("Found saved generic action: ${actionLessNotif.genericAction}")

                    val action = actionDeserializer.recreateAction(actionLessNotif.genericAction)
                    val notif = actionLessNotif.copy(
                        genericAction = null,
                        action = action,
                    )

                    entry.key to notif
                }
                .toMap() // lol

            fromJson.copy(notifs = notifs)
        } catch (ex: Exception) {
            hatchet.e("Error reading Notifs from storage: ${ex.message}")
            ex.printStackTrace()
            null
        }
    }

    companion object {
        const val KEY_NOTIF_STATE = "NotifManager.NotifState"

        private const val DEP_NAME_PREFIX = "Dep.Deserializer"

        const val DEP_NAME_JSON_ADAPTER_NOTIF = "$DEP_NAME_PREFIX.Notif"
    }
}
