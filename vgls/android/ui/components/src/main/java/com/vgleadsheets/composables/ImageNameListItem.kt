package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedCircle
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.perf.isPerfMeasurementEnabled
import net.sigmabeta.sage.ui.Icon
import kotlin.math.roundToInt

@Composable
fun ImageNameListItem(
    model: ImageNameListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    ImageNameListItem(
        model.name,
        model.sourceInfo,
        model.imagePlaceholder,
        model.clickAction,
        actionSink,
        modifier,
        padding,
    )
}

@Composable
@Suppress("MagicNumber")
fun ImageNameListItem(
    name: String,
    sourceInfo: SourceInfo,
    imagePlaceholder: Icon,
    clickAction: SageAction,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { actionSink.sendAction(clickAction) }
            .padding(paddingValues = padding)
            .padding(vertical = 4.dp)
    ) {
        ElevatedCircle(
            modifier = Modifier
                .size(ImageSize.THUMBNAIL.size)
                .align(Alignment.CenterVertically)
        ) {
            val info = sourceInfo.info
            val (actualSourceInfo, bgModifier) = if (info is PdfConfigById) {
                with(LocalDensity.current) {
                    SourceInfo(
                        info.copy(
                            maxWidth = ImageSize.THUMBNAIL.size.toPx().roundToInt(),
                            maxHeight = ImageSize.THUMBNAIL.size.toPx().roundToInt(),
                        )
                    )
                } to Modifier.background(
                    if (isPerfMeasurementEnabled) {
                        Color(1f, 1f, 0.8f, 1f)
                    } else {
                        Color.White
                    }
                )
            } else {
                sourceInfo to Modifier
            }

            CrossfadeImage(
                sourceInfo = actualSourceInfo,
                imagePlaceholder = imagePlaceholder,
                contentDescription = null,
                modifier = bgModifier,
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
private fun Light() {
    AppTheme {
        Box(
            modifier = Modifier.background(
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
    AppTheme {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
private fun Menu() {
    AppThemeMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview(fontScale = 2.0f)
@Composable
private fun Beeg() {
    AppTheme {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun Sample() {
    ImageNameListItem(
        ImageNameListModel(
            1234L,
            "Carrying the Weight of Life",
            SourceInfo("https://randomfox.ca/images/12.jpg"),
            Icon.Description,
            null,
            clickAction = SageAction.Noop,
        ),
        PreviewActionSink { },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
