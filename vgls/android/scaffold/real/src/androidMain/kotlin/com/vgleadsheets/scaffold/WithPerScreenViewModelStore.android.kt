package com.vgleadsheets.scaffold

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

// Android passthrough — Voyager already provides + retains a per-Screen LocalViewModelStoreOwner.
@Composable
internal actual fun WithPerScreenViewModelStore(screen: Screen, content: @Composable () -> Unit) {
    content()
}
