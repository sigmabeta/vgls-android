package com.vgleadsheets.scaffold

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

/**
 * Gives each Voyager [Screen] its own `ViewModelStoreOwner` so `metroViewModel<VM>()` resolves a
 * fresh VM per screen. Android is a passthrough (Voyager's `AndroidScreenLifecycleOwner` already
 * provides + retains a per-screen owner); the JVM/desktop actual hangs a [androidx.lifecycle.ViewModelStore]
 * off a Voyager `ScreenModel` keyed to [screen], since Voyager's non-Android lifecycle provider
 * supplies no per-screen owner (every screen would otherwise share the Compose Window's owner and
 * `metroViewModel` would return the same cached instance across pushes).
 */
@Composable
internal expect fun WithPerScreenViewModelStore(screen: Screen, content: @Composable () -> Unit)
