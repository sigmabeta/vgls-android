package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.SectionListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random

@Composable
@Suppress("MagicNumber")
fun SectionListItem(
    model: SectionListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        model.sectionItems.forEach {
            it.Content(
                sink = actionSink,
                debug = showDebug,
                mod = Modifier,
                pad = padding
            )
        }
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Sample()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Sample()
    }
}

@Composable
@Suppress("MagicNumber")
private fun Sample() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        val rng = Random("SectionListItem".hashCode().toLong())

        val paddingModifier = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.margin_side)
        )
        VerticalSection(rng, paddingModifier)
        VerticalSection(rng, paddingModifier)
    }
}

@Composable
@Suppress("MagicNumber")
private fun VerticalSection(rng: Random, padding: PaddingValues) {
    SectionHeader(
        name = "Vertically Scrolling Items",
        modifier = Modifier,
        padding = padding,
    )

    repeat(3) { index ->
        ImageNameListItem(
            model = ImageNameListModel(
                dataId = index.toLong(),
                name = "Wide Item #$index",
                sourceInfo = SourceInfo(rng.nextInt().toString()),
                Icon.DESCRIPTION,
                null,
                clickAction = VglsAction.Noop
            ),
            PreviewActionSink { },
            modifier = Modifier,
            padding = padding,
        )
    }
}
