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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.SinglePageListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("MagicNumber")
fun SinglePageCard(
    model: SinglePageListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Box(
        modifier = modifier
            .padding(padding)
            .fillMaxWidth()
    ) {
        SheetPageCard(
            model = model.sheetPageCardModel.sheetPageModel,
            actionSink = actionSink,
            showDebug = showDebug,
            modifier = Modifier.align(Alignment.Center),
            padding = PaddingValues()
        )
    }
}

@Preview
@Composable
private fun LightPdf() {
    VglsMaterial {
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
    VglsMaterial {
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
                    sourceInfo = SourceInfo(
                        PdfConfigById(
                            songId = 92,
                            pageNumber = 0,
                            isAltSelected = false,
                        )
                    ),
                    title = "A Trip to Alivel Mall",
                    gameName = "Kirby and the Forgotten Land",
                    composers = listOf(
                        "Hirokazu Ando",
                    ).toImmutableList(),
                    pageNumber = 0,
                    beeg = true,
                    clickAction = VglsAction.Noop,
                ),
            ),
        ),
        actionSink = PreviewActionSink { },
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.margin_side)
        ),
        showDebug = true,
        padding = PaddingValues(horizontal = 8.dp)
    )
}
