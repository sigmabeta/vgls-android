package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import com.vgleadsheets.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.EmptyStateListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon

@Composable
@Suppress("MagicNumber")
fun SheetPageCard(
    model: SheetPageListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    ElevatedCard(
        modifier = modifier
            .padding(padding)
            .wrapContentSize()
    ) {
        Box {
            SheetPageItem(
                model = model,
                actionSink = actionSink,
                showDebug = showDebug,
                padding = PaddingValues(),
                modifier = Modifier
                    .wrapContentHeight()
                    .clickable(
                        onClick = { actionSink.sendAction(model.clickAction) },
                        onClickLabel = VglsStringId.ACCY_OCL_SHEET_CARD.text()
                    ),
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
                            icon = Icon.Warning,
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
