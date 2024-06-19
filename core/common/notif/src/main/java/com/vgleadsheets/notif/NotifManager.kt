package com.vgleadsheets.notif

import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotifManager(
    private val storage: Storage
) {
    private val internalNotifState = MutableStateFlow(NotifState())
    val notifState = internalNotifState.asStateFlow()

    fun addNotif(notif: Notif) {
        internalNotifState.update {
            val newNotifsMap = it.notifs.toMutableMap()
            newNotifsMap[notif.id] = notif
            it.copy(notifs = newNotifsMap)
        }
    }

    fun removeNotif(id: Long) {
        internalNotifState.update {
            val newNotifsMap = it.notifs.toMutableMap()
            newNotifsMap.remove(id)
            it.copy(notifs = newNotifsMap)
        }
    }
}
