package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.BigImage
import com.vgleadsheets.composables.HorizontalScroller
import com.vgleadsheets.composables.SectionHeader
import com.vgleadsheets.composables.SheetPageCard
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Preview
@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier
) {
    val actionSink = ActionSink { }

    val seed = 123455L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val song = modelGenerator.randomSong()

    val padding = PaddingValues(
        horizontal = dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side)
    )

    VglsMaterial {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OnePageSheet(actionSink, padding, song)
            ComposerSection(padding, modelGenerator, actionSink)
            GameSection(padding, modelGenerator, actionSink)
        }
    }
}

@Composable
private fun ColumnScope.GameSection(
    padding: PaddingValues,
    modelGenerator: FakeModelGenerator,
    actionSink: ActionSink
) {
    Section(
        name = "From the Game",
        padding = padding,
    ) {
        val game = modelGenerator.randomGame()

        BigImage(
            model = HeroImageListModel(
                sourceInfo = game.name,
                imagePlaceholder = com.vgleadsheets.ui.Icon.ALBUM,
                name = game.name,
                caption = null,
                clickAction = VglsAction.Noop,
            ),
            actionSink = actionSink,
            modifier = Modifier,
            padding = padding,
        )
    }
}

@Composable
private fun ColumnScope.Section(
    name: String,
    padding: PaddingValues,
    content: @Composable (String) -> Unit,
) {
    SectionHeader(
        name = name,
        modifier = Modifier,
        padding = padding,
    )

    content(name)
}

@Composable
private fun OnePageSheet(
    actionSink: ActionSink,
    padding: PaddingValues,
    song: Song
) {
    SheetPageCard(
        actionSink = actionSink,
        padding = padding,
        model = SheetPageListModel(
            sourceInfo = PdfConfigById(
                songId = song.id,
                pageNumber = 0
            ),
            title = song.name,
            gameName = song.gameName,
            composers = song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
            pageNumber = 0,
            clickAction = VglsAction.Noop,
        ),
        showDebug = true,
        modifier = Modifier,
    )
}

@Composable
private fun ColumnScope.ComposerSection(
    padding: PaddingValues,
    modelGenerator: FakeModelGenerator,
    actionSink: ActionSink
) {
    Section(
        "Composers on VGLS",
        padding
    ) {
        HorizontalScroller(
            model = HorizontalScrollerListModel(
                it.hashCode().toLong(),
                modelGenerator.randomComposers()
                    .map { composer ->
                        WideItemListModel(
                            dataId = composer.id,
                            name = composer.name,
                            sourceInfo = composer.name,
                            imagePlaceholder = com.vgleadsheets.ui.Icon.PERSON,
                            actionableId = null,
                            clickAction = VglsAction.Noop,
                        )
                    }
                    .toImmutableList()
            ),
            actionSink = actionSink,
            modifier = Modifier,
            showDebug = true,
            padding = padding,
        )
    }
}
