package com.vgleadsheets.notif

import kotlinx.serialization.Serializable

@Serializable
data class NotifState(
    val notifs: Map<Long, Notif> = emptyMap()
)
