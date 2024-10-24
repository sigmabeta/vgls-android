package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.SubsectionHeaderListModel
import com.vgleadsheets.components.SubsectionListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Subsection(
    model: SubsectionListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val maxItemsInEachRow = 2
    FlowRow(
        maxItemsInEachRow = maxItemsInEachRow,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
            when (it) {
                is WideItemListModel -> WideItem(
                    model = it,
                    actionSink = actionSink,
                    modifier = Modifier.weight(1.0f),
                    padding = PaddingValues()
                )

                is SquareItemListModel -> SquareItem(
                    model = it,
                    actionSink = actionSink,
                    modifier = Modifier,
                    padding = padding,
                )

                else -> TODO()
            }
        }

        val spacerCount = model.children.size % maxItemsInEachRow

        for (i in 0..spacerCount) {
            Spacer(Modifier.weight(1.0f))
        }
    }
}

@Preview
@Composable
private fun LightWide() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            SampleWide()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkWide() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            SampleWide()
        }
    }
}

@Preview
@Composable
private fun LightSquare() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            SampleSquare()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkSquare() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(16.dp)
        ) {
            SampleSquare()
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun SampleWide() {
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
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
                WideItemListModel(
                    3456L,
                    "Yasunori Mitsuda",
                    null,
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
                WideItemListModel(
                    4567L,
                    "ACE+",
                    null,
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
            ).toImmutableList()
        ),
        PreviewActionSink { },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}

@Composable
@Suppress("MagicNumber")
private fun SampleSquare() {
    Subsection(
        SubsectionListModel(
            1234L,
            SubsectionHeaderListModel(
                "Composers for this game on VGLS",
            ),
            listOf(
                SquareItemListModel(
                    2345L,
                    "Manami Kiyota",
                    null,
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
                SquareItemListModel(
                    3456L,
                    "Yasunori Mitsuda",
                    null,
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
                SquareItemListModel(
                    4567L,
                    "ACE+",
                    null,
                    Icon.PERSON,
                    actionableId = null,
                    clickAction = VglsAction.Noop,
                ),
            ).toImmutableList()
        ),
        PreviewActionSink { },
        Modifier,
        PaddingValues(horizontal = 8.dp)
    )
}
