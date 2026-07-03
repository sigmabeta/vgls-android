package com.vgleadsheets.ui.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.appcomm.ActionSink

/**
 * Desktop placeholder for the sheet viewer. The android renderer is telephoto sub-sampling over a
 * [android.graphics.pdf.PdfRenderer] bitmap; the JVM target has neither, so there is nothing to
 * pinch-zoom yet. Showing a black placeholder keeps navigation working (the viewer route still
 * pushes/pops) without pulling telephoto — which publishes no JVM variant — into commonMain.
 */
@Composable
actual fun ViewerScreen(
    state: ViewerState,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Text(
            text = "Sheet viewing isn't available on the desktop app yet.",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(32.dp),
        )
    }
}
