package com.vgleadsheets.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.pdf.fake.FakeBitmapRenderer
import java.io.File
import kotlin.random.Random

@Composable
fun FakeBitmapDisplayer(
    modifier: Modifier = Modifier
) {
    val bitmapRenderer = remember { FakeBitmapRenderer() }

    ElevatedRoundRect(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        val bitmap = bitmapRenderer.renderToBitmap(
            // File("ttewy"),
            // File("ty"),
            File("ty89123"),
            // null,
            0,
            width = 160,
            height = 160,
            zoom = 1.0f,
        ).asImageBitmap()

        Image(
            painter = BitmapPainter(
                image = bitmap,
                filterQuality = FilterQuality.None,
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
private fun Light() {
    val randomizer = Random(RANDOMIZER_SEED)
    FullScreenOf { paddingValues ->
        Sample(randomizer.nextLong(), paddingValues)
    }
}

@Preview
@Composable
private fun Dark() {
    val randomizer = Random(RANDOMIZER_SEED)
    FullScreenOf(darkTheme = true) { paddingValues ->
        Sample(randomizer.nextLong(), paddingValues)
    }
}

@Composable
private fun Sample(
    seed: Long,
    paddingValues: PaddingValues,
) {
    FakeBitmapDisplayer(
        modifier = Modifier.padding(paddingValues)
    )
}

private const val RANDOMIZER_SEED = 12301L
