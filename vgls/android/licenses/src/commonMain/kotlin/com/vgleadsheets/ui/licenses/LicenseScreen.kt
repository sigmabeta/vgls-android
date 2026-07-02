package com.vgleadsheets.ui.licenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Renders the open-source licenses. Android shows the licenses page in a WebView; the JVM/desktop
 * target shows a placeholder for now (a native WebView equivalent is a follow-up).
 */
@Composable
expect fun LicenseScreen(
    state: State,
    modifier: Modifier,
)
