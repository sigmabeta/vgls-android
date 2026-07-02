package com.vgleadsheets.composables

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun DeviceBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)
