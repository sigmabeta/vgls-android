package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.R
import com.vgleadsheets.composables.subs.ElevatedCircle
import com.vgleadsheets.composables.subs.ElevatedPill
import com.vgleadsheets.composables.subs.Flasher
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.themes.VglsMaterialMenu
import kotlin.random.Random

@Composable
fun LoadingListItem(
    withImage: Boolean,
    seed: Long,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = dimensionResource(id = R.dimen.margin_side)
            )
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
            ElevatedPill(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(12.dp)
                    .fillMaxWidth(randomizer.next())
            ) {
                Flasher(startDelay = randomDelay + 100)
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            ElevatedPill(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 12.dp)
                    .height(10.dp)
                    .fillMaxWidth(randomizer.next())
            ) {
                Flasher(startDelay = randomDelay + 200)
            }
        }
    }
}

private fun Random.next() = nextFloat().coerceAtLeast(0.3f)

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(
                1845L
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(2345L)
        }
    }
}

@Preview
@Composable
private fun LightWithImage() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleWithImage(5678L)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkWithImage() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleWithImage(2345L)
        }
    }
}

@Preview
@Composable
private fun Menu() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleWithImage(5678L)
        }
    }
}

@Composable
private fun Sample(seed: Long) {
    LoadingListItem(
        withImage = false,
        seed = seed,
        modifier = Modifier
    )
}

@Composable
private fun SampleWithImage(seed: Long) {
    LoadingListItem(
        withImage = true,
        seed = seed,
        modifier = Modifier
    )
}
