package com.vgleadsheets.jvm

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Whether the desktop window currently has OS focus. DesktopMain feeds it from Compose's
 * `LocalWindowInfo.isWindowFocused`; background work that only matters while the user is looking
 * (e.g. [JvmNetworkStatusProvider]'s polling) observes [focused] instead of depending on AWT/Compose.
 */
class JvmWindowFocus {
    private val _focused = MutableStateFlow(false)
    val focused: StateFlow<Boolean> = _focused.asStateFlow()

    fun setFocused(focused: Boolean) {
        _focused.value = focused
    }
}
