package com.vgleadsheets.remaster.composers.detail

import android.content.res.Resources
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composers.detail.R
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Song
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun State.toListItems(resources: Resources): ImmutableList<ListModel> {
    val composerModel = if (composer?.photoUrl != null) {
        listOf<ListModel>(
            HeroImageListModel(
                imageUrl = composer.photoUrl!!,
                imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
                name = composer.name,
            ) { }
        )
    } else {
        emptyList()
    }

    val gameModels = if (games.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_games)
            ),
            HorizontalScrollerListModel(
                dataId = R.string.section_header_games + ID_PREFIX_SCROLLER_CONTENT,
                scrollingItems = games.map { composer ->
                    WideItemListModel(
                        dataId = composer.id + ID_PREFIX_GAMES,
                        name = composer.name,
                        imageUrl = composer.photoUrl,
                        imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
                        onClick = { }
                    )
                }
            )
        )
    } else {
        emptyList()
    }

    val songModels = if (songs.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_songs_from_composer)
            )
        ) + songs.map { song ->
            val imageUrl = song.thumbUrl(sheetUrlInfo.imageBaseUrl, sheetUrlInfo.partId)
            ImageNameListModel(
                song.id + ID_PREFIX_SONGS,
                song.name,
                imageUrl,
                com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp
            ) { }
        }
    } else {
        emptyList()
    }

    return (composerModel + gameModels + songModels).toPersistentList()
}

private fun Song.thumbUrl(baseImageUrl: String?, selectedPart: String?): String? {
    return Page.generateImageUrl(
        baseImageUrl ?: return null,
        selectedPart ?: return null,
        filename,
        isAltSelected,
        1
    )
}

private fun List<Song>.captionText(resources: Resources) = resources.getString(
    R.string.subtitle_sheets_count,
    size
)

private const val ID_PREFIX_GAMES = 1_000_000L
private const val ID_PREFIX_SONGS = 1_000_000_000L
private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
