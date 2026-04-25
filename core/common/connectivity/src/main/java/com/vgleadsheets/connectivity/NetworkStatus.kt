package com.vgleadsheets.connectivity

enum class NetworkStatus {
    OFFLINE,

    /** Network transport is up but internet has not been validated (captive portal, broken backhaul, etc.). */
    ONLINE_NO_INTERNET,

    ONLINE,
}
