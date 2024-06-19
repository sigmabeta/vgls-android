package com.vgleadsheets.notif

data class NotifState(
    val notifs: Map<Long, Notif> = emptyMap()
)
