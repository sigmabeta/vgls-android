package com.vgleadsheets.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.FullScreenOf
import com.vgleadsheets.composables.subs.ElevatedCircle
import com.vgleadsheets.composables.subs.ElevatedPill
import com.vgleadsheets.composables.subs.Flasher
import kotlin.random.Random

@Composable
@Suppress("MagicNumber")
fun LoadingListItem(
    withImage: Boolean,
    withCaption: Boolean,
    seed: Long,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
    ) {
        val randomizer = Random(seed)
        val randomDelay = randomizer.nextInt(200)

        if (withImage) {
            ElevatedCircle(
                Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Flasher(startDelay = randomDelay)
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val paddingModifier = if (withCaption) {
                Modifier.padding(top = 12.dp)
            } else {
                Modifier.padding(vertical = 16.dp)
            }
            ElevatedPill(
                modifier = paddingModifier
                    .height(14.dp)
                    .fillMaxWidth(randomizer.next())
            ) {
                Flasher(startDelay = randomDelay)
            }

            if (withCaption) {
                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                ElevatedPill(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 12.dp)
                        .height(10.dp)
                        .fillMaxWidth(randomizer.next())
                ) {
                    Flasher(startDelay = randomDelay)
                }
            }
        }
    }
}

@Suppress("MagicNumber")
private fun Random.next() = nextFloat().coerceAtLeast(0.3f)

@Preview
@Composable
private fun Light() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            withImage = false,
            withCaption = false,
        )
    }
}

@Preview
@Composable
private fun LightWithImage() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            withImage = true,
            withCaption = false,
        )
    }
}

@Preview
@Composable
private fun LightWithImageAndCaption() {
    FullScreenOf {
        Sample(
            seed = Random.nextLong(),
            withImage = true,
            withCaption = true,
        )
    }
}

@Preview
@Composable
private fun DarkWithImageAndCaption() {
    FullScreenOf(darkTheme = true) {
        Sample(
            seed = Random.nextLong(),
            withImage = true,
            withCaption = true,
        )
    }
}

@Composable
private fun Sample(seed: Long, withImage: Boolean, withCaption: Boolean) {
    LoadingListItem(
        withImage = withImage,
        withCaption = withCaption,
        seed = seed,
        modifier = Modifier,
        padding = PaddingValues(horizontal = 16.dp)
    )
}
