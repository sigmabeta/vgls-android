package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
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
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = game?.name,
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val gameModels = if (game?.photoUrl != null) {
            listOf<ListModel>(
                HeroImageListModel(
                    sourceInfo = game.photoUrl!!,
                    imagePlaceholder = Icon.ALBUM,
                    name = null,
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
                            sourceInfo = composer.photoUrl,
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
                val sourceInfo = PdfConfigById(
                    songId = song.id,
                    partApiId = sheetUrlInfo.partId ?: "",
                    pageNumber = 1
                )

                ImageNameListModel(
                    dataId = song.id + ID_PREFIX_SONGS,
                    name = song.name,
                    sourceInfo = sourceInfo,
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id)
                )
            }
        } else {
            emptyList()
        }

        return (gameModels + composerModels + songModels).toPersistentList()
    }

    companion object {
        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
