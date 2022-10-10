package com.vgleadsheets.database

internal fun <ReturnType> Boolean.nullWhenFalse(function: () -> ReturnType): ReturnType? {
    return if (this) {
        function()
    } else {
        null
    }
}
