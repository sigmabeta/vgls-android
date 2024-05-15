package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.images.Page
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val title: String? = null,
    val sheetUrlInfo: UrlInfo = UrlInfo(),
    val composer: Composer? = null,
    val songs: List<Song> = emptyList(),
    val games: List<Game> = emptyList(),
) : ListState() {
    override fun title() = composer?.name

    override fun toListItems(stringProvider: StringProvider, actionHandler: (ListAction) -> Unit): ImmutableList<ListModel> {
        val composerModel = if (composer?.photoUrl != null) {
            listOf<ListModel>(
                HeroImageListModel(
                    imageUrl = composer.photoUrl!!,
                    imagePlaceholder = Icon.PERSON,
                    name = composer.name,
                ) { }
            )
        } else {
            emptyList()
        }

        val gameModels = if (games.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_COMPOSER)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_GAMES_FROM_COMPOSER.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = games.map { game ->
                        WideItemListModel(
                            dataId = game.id + ID_PREFIX_GAMES,
                            name = game.name,
                            imageUrl = game.photoUrl,
                            imagePlaceholder = Icon.ALBUM,
                            onClick = { actionHandler(Action.GameClicked(game.id))}
                        )
                    }.toImmutableList()
                )
            )
        } else {
            emptyList()
        }

        val songModels = if (songs.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_COMPOSER)
                )
            ) + songs.map { song ->
                val imageUrl = song.thumbUrl(sheetUrlInfo.imageBaseUrl, sheetUrlInfo.partId)
                ImageNameListModel(
                    song.id + ID_PREFIX_SONGS,
                    song.name,
                    imageUrl,
                    Icon.PERSON
                ) { actionHandler(Action.SongClicked(song.id)) }
            }
        } else {
            emptyList()
        }

        return (composerModel + gameModels + songModels).toImmutableList()
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

    companion object {
        private const val ID_PREFIX_GAMES = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
