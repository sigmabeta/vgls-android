package com.vgleadsheets.remaster.games.detail

import android.content.res.Resources
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.games.detail.R
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun State.toListItems(resources: Resources): ImmutableList<ListModel> {
    val gameModels = if (game?.photoUrl != null) {
        listOf<ListModel>(
            HeroImageListModel(
                imageUrl = game.photoUrl!!,
                imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
                name = game.name,
            ) { }
        )
    } else {
        emptyList()
    }

    val composerModels = if (composers.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_composers)
            )
        ) + composers.map { composer ->
            WideItemListModel(
                dataId = composer.id + ID_PREFIX_COMPOSERS,
                name = composer.name,
                imageUrl = composer.photoUrl,
                imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
                onClick = { }
            )
        }
    } else {
        emptyList()
    }

    val songModels = if (songs.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_songs_from_game)
            )
        ) + songs.map { song ->
            ImageNameListModel(
                song.id + ID_PREFIX_SONGS,
                song.name,
                song.thumbUrl(baseImageUrl, selectedPart),
                com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp
            ) { }
        }
    } else {
        emptyList()
    }

    return (gameModels + composerModels + songModels).toPersistentList()
}

private fun Song.thumbUrl(baseImageUrl: String?, selectedPart: Part?): String? {
    return Page.generateImageUrl(
        baseImageUrl ?: return null,
        selectedPart?.apiId ?: return null,
        filename,
        isAltSelected,
        1
    )
}

private fun List<Song>.captionText(resources: Resources) = resources.getString(
    R.string.subtitle_sheets_count,
    size
)

private const val ID_PREFIX_COMPOSERS = 1_000_000L
private const val ID_PREFIX_SONGS = 1_000_000_000L
