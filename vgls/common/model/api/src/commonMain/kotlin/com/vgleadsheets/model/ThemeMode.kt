package com.vgleadsheets.model

/**
 * User-selectable app theme. [SYSTEM] follows the OS light/dark setting and is the default;
 * [LIGHT] / [DARK] pin the respective color scheme regardless of the system setting.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
    ;

    companion object {
        val DEFAULT: ThemeMode = SYSTEM

        /** Parses a persisted [value] back into a [ThemeMode], falling back to [DEFAULT]. */
        fun fromStorageValue(value: String?): ThemeMode = entries.firstOrNull { it.name == value } ?: DEFAULT
    }
}
