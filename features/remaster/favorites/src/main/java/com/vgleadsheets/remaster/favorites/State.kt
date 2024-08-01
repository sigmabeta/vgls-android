package com.vgleadsheets.remaster.favorites

import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val favoriteSongs: List<Song> = emptyList(),
    val favoriteGames: List<Game> = emptyList(),
    val favoriteComposers: List<Composer> = emptyList(),
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_FAVORITES)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return (songItems(stringProvider) + gameItems(stringProvider) + composerItems(stringProvider))
            .toImmutableList()
    }

    private fun songItems(stringProvider: StringProvider) = if (favoriteSongs.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_SONGS)
            )
        ) + favoriteSongs.map { song ->
            ImageNameCaptionListModel(
                dataId = song.id,
                name = song.name,
                caption = song.gameName,
                sourceInfo = PdfConfigById(
                    songId = song.id,
                    pageNumber = 0
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(song.id),
            )
        }
    } else {
        emptyList()
    }

    private fun gameItems(stringProvider: StringProvider) = if (favoriteGames.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_GAMES)
            )
        ) + favoriteGames.map { game ->
            SquareItemListModel(
                dataId = game.id + ID_OFFSET_GAME,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.ALBUM,
                clickAction = Action.GameClicked(game.id),
            )
        }
    } else {
        emptyList()
    }

    private fun composerItems(stringProvider: StringProvider) = if (favoriteComposers.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_COMPOSERS)
            )
        ) + favoriteComposers.map { composer ->
            SquareItemListModel(
                dataId = composer.id + ID_OFFSET_COMPOSER,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }
    } else {
        emptyList()
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
