package com.vgleadsheets.composables.subs

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.vgleadsheets.composables.ImageNameListItem
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.images.BitmapGenerator
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.ui.vector

@Preview
@Composable
private fun Light() {
    AppTheme {
        Sample()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    AppTheme {
        Sample()
    }
}

@Composable
@Suppress("LongMethod", "MagicNumber")
private fun Sample() {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        ImageNameListItem(
            ImageNameListModel(
                1234L,
                "Carrying the Weight of Life",
                SourceInfo(info = null),
                Icon.Description,
                null,
                clickAction = SageAction.Noop,
            ),
            PreviewActionSink { },
            Modifier,
            PaddingValues(horizontal = 8.dp)
        )

        Row {
            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("etc"),
                    imagePlaceholder = Icon.Person,
                    contentDescription = null,
                    simulateError = true,
                    forceGenBitmap = false,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.Description,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedRoundRect(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                cornerRadius = 4.dp
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.Description,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }
        }

        Row {
            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("etc"),
                    imagePlaceholder = Icon.Person,
                    contentDescription = null,
                    simulateError = true,
                    forceGenBitmap = false,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo(null),
                    imagePlaceholder = Icon.Description,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }

            ElevatedCircle(
                Modifier.size(64.dp)
            ) {
                CrossfadeImage(
                    sourceInfo = SourceInfo("doesn't matter"),
                    imagePlaceholder = Icon.Description,
                    contentDescription = null,
                    modifier = Modifier,
                )
            }
        }
    }
}
