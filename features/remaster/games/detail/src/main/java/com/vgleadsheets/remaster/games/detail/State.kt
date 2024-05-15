package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.images.Page
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

data class State(
    val title: String? = null,
    val sheetUrlInfo: UrlInfo = UrlInfo(),
    val game: Game? = null,
    val songs: List<Song> = emptyList(),
    val composers: List<Composer> = emptyList(),
) : ListState() {
    override fun title() = game?.name

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val gameModels = if (game?.photoUrl != null) {
            listOf<ListModel>(
                HeroImageListModel(
                    imageUrl = game.photoUrl!!,
                    imagePlaceholder = Icon.ALBUM,
                    name = game.name,
                    clickAction = VglsAction.Noop,
                )
            )
        } else {
            emptyList()
        }

        val composerModels = if (composers.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_GAME)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_GAME.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = composers.map { composer ->
                        WideItemListModel(
                            dataId = composer.id + ID_PREFIX_COMPOSERS,
                            name = composer.name,
                            imageUrl = composer.photoUrl,
                            imagePlaceholder = Icon.PERSON,
                            clickAction = Action.ComposerClicked(composer.id)
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
                    stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_GAME)
                )
            ) + songs.map { song ->
                val imageUrl = song.thumbUrl(sheetUrlInfo.imageBaseUrl, sheetUrlInfo.partId)
                ImageNameListModel(
                    dataId = song.id + ID_PREFIX_SONGS,
                    name = song.name,
                    imageUrl = imageUrl,
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id)
                )
            }
        } else {
            emptyList()
        }

        return (gameModels + composerModels + songModels).toPersistentList()
    }

    private fun Song.thumbUrl(baseImageUrl: String?, selectedPart: String?): String? {
        return Page.generateImageUrl(
            baseImageUrl = baseImageUrl ?: return null,
            partApiId = selectedPart ?: return null,
            filename = filename,
            isAlternateEnabled = isAltSelected,
            pageNumber = 1
        )
    }

    companion object {
        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
