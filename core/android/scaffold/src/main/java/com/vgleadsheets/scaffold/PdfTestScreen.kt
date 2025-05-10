package com.vgleadsheets.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.composables.LabelValueListItem
import com.vgleadsheets.composables.PdfDisplayer
import java.util.Locale

@Composable
fun PdfTestScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        var zoom by remember { mutableFloatStateOf(4f) }
        var zoomTemporary by remember { mutableFloatStateOf(4f) }

        PdfDisplayer(
            pdfPath = "/data/user/0/com.vgleadsheets.debug/files/pdfs/Aerobiz - Europe/C.pdf",
            zoom = zoom
        )
        val bottomInset = WindowInsets.navigationBars.asPaddingValues()

        // Information view
        Column(
            modifier = Modifier
                .alpha(0.8f)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 8.dp)
                .padding(bottom = bottomInset.calculateBottomPadding())
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Slider(
                value = zoomTemporary,
                valueRange = 0.5f .. 8f,
                onValueChange = { zoomTemporary = it },
                onValueChangeFinished = {
                    zoom = zoomTemporary
                }
            )

            val zoomString = String.format(Locale.getDefault(), "%.2f", zoom)
            LabelValueListItem(
                LabelValueListModel(
                    label = "Zoom",
                    value = zoomString,
                    clickAction = VglsAction.Noop,
                ),
                actionSink = { },
                modifier = Modifier,
                padding = PaddingValues()
            )
        }
    }
}
