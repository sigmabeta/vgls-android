package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.PdfTestListModel
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun PdfTestListItem(
    model: PdfTestListModel,
    modifier: Modifier,
) {
    ElevatedCard(
        modifier = Modifier.padding(16.dp)
    ) {
        AsyncImage(
            model = PdfConfigById(
                model.songId,
                model.partApiId,
                model.pageNumber
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PortraitPdf() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            SamplePdf()
        }
    }
}

@Composable
private fun SamplePdf() {
    PdfTestListItem(
        PdfTestListModel(
            songId = 92,
            partApiId = "Eb",
            pageNumber = 1,
            clickAction = VglsAction.Noop,
        ),
        modifier = Modifier,
    )
}
