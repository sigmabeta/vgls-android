package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.SectionHeader
import com.vgleadsheets.composables.SheetPageCard
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

    val seed = 12345L
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
                modifier = Modifier,
            )

            SectionHeader(
                name = "Composers on VGLS",
                modifier = Modifier,
                padding = padding,
            )
        }
    }
}
