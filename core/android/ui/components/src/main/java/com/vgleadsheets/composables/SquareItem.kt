package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.previews.SquareConstants
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
@Suppress("MagicNumber")
fun SquareItem(
    model: SquareItemListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    ElevatedRoundRect(
        modifier = modifier
            .padding(paddingValues = padding)
            .defaultMinSize(minWidth = SquareConstants.MIN_WIDTH)
            .aspectRatio(SquareConstants.ASPECT_RATIO)
            .clickable { actionSink.sendAction(model.clickAction) }
    ) {
        Box {
            CrossfadeImage(
                sourceInfo = SourceInfo(model.sourceInfo),
                imagePlaceholder = model.imagePlaceholder,
                contentDescription = model.name,
                modifier = Modifier
                    .fillMaxSize()
            )

            Text(
                text = model.name,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0, 0, 0, 0),
                                Color(0, 0, 0, 160),
                                Color(0, 0, 0, 255),
                            )
                        )
                    )
                    .padding(8.dp)
                    .padding(top = 8.dp) // For extra scrim
            )
        }
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Sample()
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun Sample() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        SquareItem(
            SquareItemListModel(
                1234L,
                "Xenoblade Chronicles 3",
                "https://randomfox.ca/images/12.jpg",
                Icon.ALBUM,
                null,
                VglsAction.Noop
            ),
            PreviewActionSink {},
            Modifier.weight(1.0f),
            PaddingValues(horizontal = 8.dp)
        )

        SquareItem(
            SquareItemListModel(
                1234L,
                "Xenoblade Chronicles 3: Future Redeemed Some More",
                "https://randomfox.ca/images/1235.jpg",
                Icon.ALBUM,
                null,
                VglsAction.Noop
            ),
            PreviewActionSink {},
            Modifier.weight(1.0f),
            PaddingValues(horizontal = 8.dp)
        )
    }
}
