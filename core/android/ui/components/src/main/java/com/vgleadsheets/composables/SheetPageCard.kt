package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("MagicNumber")
fun SheetPageCard(
    model: SheetPageListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val maxHeight = if (model.beeg) {
        600.dp
    } else {
        400.dp
    }

    ElevatedCard(
        modifier = modifier
            .heightIn(max = maxHeight)
            .padding(padding)
            .padding(bottom = 16.dp)
            .wrapContentSize()
    ) {
        Box {
            SheetPageItem(
                model = model,
                actionSink = actionSink,
                fillMaxWidth = false,
                showDebug = showDebug,
                padding = PaddingValues(),
                modifier = Modifier
                    .wrapContentHeight()
                    .clickable { actionSink.sendAction(model.clickAction) },
            )

            if (model.showLyricsWarning) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0, 0, 0, 128))
                        .padding(horizontal = 8.dp),
                ) {
                    EmptyListIndicator(
                        model = EmptyStateListModel(
                            icon = Icon.WARNING,
                            explanation = "No lyrics available for this sheet.",
                            showCrossOut = false,
                        ),
                        onBlack = true,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
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
    SheetPageCard(
        model = SheetPageListModel(
            sourceInfo = PdfConfigById(
                songId = 92,
                pageNumber = 0,
                isAltSelected = false,
            ),
            title = "A Trip to Alivel Mall",
            gameName = "Kirby and the Forgotten Land",
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList(),
            pageNumber = 0,
            beeg = false,
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.margin_side)
        ),
        showDebug = true,
        padding = PaddingValues(horizontal = 8.dp)
    )
}
