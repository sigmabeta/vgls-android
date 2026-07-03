package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.SheetPageCardListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.components.SinglePageListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.pdf.PdfConfigById

@Preview
@Composable
private fun LightPdf() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            SamplePdf()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkPdf() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            SamplePdf()
        }
    }
}

@Composable
private fun SamplePdf() {
    SinglePageCard(
        model = SinglePageListModel(
            sheetPageCardModel = SheetPageCardListModel(
                sheetPageModel = SheetPageListModel(
                    pdfConfigById = PdfConfigById(
                        songId = 92,
                        pageNumber = 0,
                        isAltSelected = false,
                        pdfSize = PdfSize.MEDIUM,
                    ),
                    title = "A Trip to Alivel Mall",
                    gameName = "Kirby and the Forgotten Land",
                    composers = listOf(
                        "Hirokazu Ando",
                    ).toImmutableList(),
                    pageNumber = 0,
                    clickAction = SageAction.Noop,
                ),
            ),
        ),
        actionSink = PreviewActionSink { },
        modifier = Modifier.padding(
            horizontal = Dimensions.marginSide
        ),
        showDebug = true,
        padding = PaddingValues(horizontal = 8.dp)
    )
}
