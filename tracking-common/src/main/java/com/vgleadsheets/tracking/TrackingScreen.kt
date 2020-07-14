package com.vgleadsheets.tracking

enum class TrackingScreen {
    // WARNING: do NOT reorder these! These are used for tracking and
    // you could screw that up!!!
    HUD,
    GAME_LIST,
    COMPOSER_LIST,
    SHEET_LIST,
    SHEET_VIEWER,
    TAG_VALUE_LIST,
    JAM_DETAIL,
    ABOUT,
    LICENSE,
    DEBUG,
    COMPOSER_DETAIL,
    GAME_DETAIL,
    JAM_LIST,
    SETTINGS,
    SEARCH,
    SHEET_DETAIL,
    TAG_KEY_LIST,
    TAG_VALUE_SONG_LIST,

    // For top-level screens to be able to report that no one else launched them
    NONE,

    // TODO
    DEEPLINK
}
