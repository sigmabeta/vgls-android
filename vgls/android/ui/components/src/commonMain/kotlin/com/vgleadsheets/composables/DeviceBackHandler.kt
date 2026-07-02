package com.vgleadsheets.composables

import androidx.compose.runtime.Composable

/**
 * Multiplatform device-back handler. On Android this delegates to
 * `androidx.activity.compose.BackHandler`; on the JVM/desktop it's a no-op (desktop routes back at
 * the window level — Escape/Backspace — rather than per-composable).
 */
@Composable
expect fun DeviceBackHandler(enabled: Boolean, onBack: () -> Unit)
