package com.vgleadsheets.notif

import kotlinx.serialization.Serializable

@Serializable
enum class NotifCategory {
    APP_UPDATE,
    VGLS_UPDATE,
    ERROR,
    OTHER
}
