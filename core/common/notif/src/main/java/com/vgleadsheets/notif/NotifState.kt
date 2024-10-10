package com.vgleadsheets.notif

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotifState(
    val notifs: Map<Long, Notif> = emptyMap()
)
