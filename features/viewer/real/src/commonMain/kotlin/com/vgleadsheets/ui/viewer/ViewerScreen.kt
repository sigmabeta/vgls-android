package com.vgleadsheets.ui.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.sigmabeta.sage.appcomm.ActionSink

/**
 * The sheet viewer. Its rendering path is telephoto-backed pinch-zoom over a PDF page bitmap
 * (android [android.graphics.pdf.PdfRenderer]) — neither of which exists off-Android — so the
 * composable is expect/actual: the android actual is the real [SheetPager]/zoomable-image
 * implementation, and the JVM/desktop actual is a placeholder (no PDF rendering there yet).
 */
@Composable
expect fun ViewerScreen(
    state: ViewerState,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
)
