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
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.components.SectionListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon
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
                clickAction = SageAction.Noop
            ),
            PreviewActionSink { },
            modifier = Modifier,
            padding = padding,
        )
    }
}
