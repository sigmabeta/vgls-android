package com.vgleadsheets.composables

import androidx.compose.runtime.Composable

// Desktop routes back at the window level (Escape/Backspace), so per-composable back is a no-op.
@Composable
actual fun DeviceBackHandler(enabled: Boolean, onBack: () -> Unit) {
}
