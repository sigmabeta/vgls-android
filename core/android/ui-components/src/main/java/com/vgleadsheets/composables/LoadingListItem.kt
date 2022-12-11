package com.vgleadsheets.composables

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
import kotlin.random.Random

@Composable
fun LoadingListItem(seed: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = dimensionResource(id = R.dimen.margin_side)
            )
    ) {
        ElevatedCircle(
            Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
        ) {
            Flasher()
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val widthRandomizer = Random(seed)

            ElevatedPill(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(12.dp)
                    .fillMaxWidth(widthRandomizer.next())
            ) {
                Flasher(startDelay = 100)
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            ElevatedPill(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 12.dp)
                    .height(10.dp)
                    .fillMaxWidth(widthRandomizer.next())
            ) {
                Flasher(startDelay = 200)
            }
        }
    }
}

private fun Random.next() = nextFloat().coerceAtLeast(0.3f)

@Preview
@Composable
private fun LightOne() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(1845L)
        }
    }
}

@Preview
@Composable
private fun LightTwo() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(5678L)
        }
    }
}

@Preview
@Composable
private fun Dark() {
    VglsMaterial(useDarkTheme = true) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(2345L)
        }
    }
}

@Composable
private fun Sample(seed: Long) {
    LoadingListItem(seed)
}
