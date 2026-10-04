package com.vgleadsheets.composables.previews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.ui.theme.AppTheme
import kotlinx.coroutines.awaitCancellation
import net.sigmabeta.sage.images.ImageCollage
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

/**
 * Collages of 1-4 images rendered through [CrossfadeImage]'s real (Coil) path, not the fake-bitmap
 * path every other preview takes, so the screenshot catches layout bugs that only show once tiles
 * are wrapped in `Crossfade`. Coil's preview handler never finishes loading, so every tile shows
 * its placeholder icon (which outlines its bounds) instead of racing a load against the snapshot.
 */
@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
internal fun CollageImages(darkTheme: Boolean = false) {
    val previewHandler = AsyncImagePreviewHandler { awaitCancellation() }

    AppTheme(forceDark = darkTheme) {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                for (tileCount in 1..ImageCollage.MAX_SOURCES) {
                    CrossfadeImage(
                        sourceInfo = SourceInfo.ofUrls(List(tileCount) { "https://example.com/$it" }),
                        imagePlaceholder = Icon.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        forceGenBitmap = false,
                    )
                }
            }
        }
    }
}
