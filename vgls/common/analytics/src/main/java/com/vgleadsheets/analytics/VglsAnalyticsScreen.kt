package com.vgleadsheets.analytics

import net.sigmabeta.sage.analytics.AnalyticsScreenId

enum class VglsAnalyticsScreen : AnalyticsScreenId {
    // WARNING: do NOT reorder these! These are used for analytics and
    // you could screw that up!!!
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
    HOME,
    BROWSE,
    PART_PICKER,
    LIST_DIFFICULTY_TYPES,
    LIST_DIFFICULTY_VALUES,
    UPDATES,
    LIST_OFFLINE,
    OFFLINE_UPDATES,
}
