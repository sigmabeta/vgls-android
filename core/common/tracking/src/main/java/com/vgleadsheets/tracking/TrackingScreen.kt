package com.vgleadsheets.tracking

enum class TrackingScreen {
    // WARNING: do NOT reorder these! These are used for tracking and
    // you could screw that up!!!
    HUD,
    LIST_FAVORITE,
    LIST_GAME,
    LIST_COMPOSER,
    LIST_SHEET,
    LIST_JAM,
    LIST_TAG_VALUE,
    LIST_TAG_KEY,
    LIST_TAG_VALUE_SONG,
    DETAIL_GAME,
    DETAIL_COMPOSER,
    DETAIL_JAM,
    DETAIL_SHEET,
    SHEET_VIEWER,
    SEARCH,
    SETTINGS,
    LICENSE,
    ABOUT,
    DEBUG,

    // For top-level screens to be able to report that no one else launched them
    NONE,

    // TODO
    DEEPLINK
}
