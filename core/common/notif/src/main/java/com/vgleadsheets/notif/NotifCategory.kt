package com.vgleadsheets.notif

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class NotifCategory {
    APP_UPDATE,
    VGLS_UPDATE,
    ERROR,
    OTHER
}
