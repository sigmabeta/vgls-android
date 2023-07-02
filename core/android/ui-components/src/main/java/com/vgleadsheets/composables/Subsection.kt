package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.SubsectionHeaderListModel
import com.vgleadsheets.components.SubsectionListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.vectors.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Subsection(
    model: SubsectionListModel,
    modifier: Modifier
) {
    val maxItemsInEachRow = 2
    FlowRow(
        maxItemsInEachRow = maxItemsInEachRow,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(8.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp)
    ) {
        SubsectionHeader(
            model = model.titleModel,
            modifier = Modifier
        )

        model.children.forEach {
            WideItem(
                model = it as WideItemListModel,
                modifier = Modifier,
            )
        }

        val spacerCount = model.children.size % maxItemsInEachRow

        for (i in 0..spacerCount) {
            Spacer(Modifier.weight(1.0f))
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
                .padding(16.dp)
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
                .padding(16.dp)
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    Subsection(
        SubsectionListModel(
            1234L,
            SubsectionHeaderListModel(
                "Composers for this game on VGLS",
            ),
            listOf(
                WideItemListModel(
                    2345L,
                    "Manami Kiyota",
                    null,
                    R.drawable.ic_person_24dp,
                    actionableId = null,
                    onClick = { },
                ),
                WideItemListModel(
                    3456L,
                    "Yasunori Mitsuda",
                    null,
                    R.drawable.ic_person_24dp,
                    actionableId = null,
                    onClick = { },
                ),
                WideItemListModel(
                    4567L,
                    "ACE+",
                    null,
                    R.drawable.ic_person_24dp,
                    actionableId = null,
                    onClick = { },
                ),
            )
        ),
        Modifier,
    )
}

