package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
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

data class State(
    val title: String? = null,
    val sheetUrlInfo: UrlInfo = UrlInfo(),
    val composer: Composer? = null,
    val songs: List<Song> = emptyList(),
    val games: List<Game> = emptyList(),
    val isFavorite: Boolean? = null,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = composer?.name,
    )

    @Suppress("LongMethod")
    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val composerModel = if (composer?.photoUrl != null) {
            listOf<ListModel>(
                HeroImageListModel(
                    sourceInfo = composer.photoUrl!!,
                    imagePlaceholder = Icon.PERSON,
                    name = null,
                    clickAction = VglsAction.Noop,
                )
            )
        } else {
            emptyList()
        }

        val ctaModels = if (composer != null && isFavorite != null) {
            val (icon, label, action) = if (isFavorite) {
                Triple(
                    Icon.JAM_FILLED,
                    StringId.CTA_FAVORITE_REMOVE,
                    Action.RemoveFavoriteClicked,
                )
            } else {
                Triple(
                    Icon.JAM_EMPTY,
                    StringId.CTA_FAVORITE_ADD,
                    Action.AddFavoriteClicked,
                )
            }

            listOf(
                CtaListModel(
                    icon = icon,
                    name = stringProvider.getString(label),
                    clickAction = action,
                )
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
                            sourceInfo = game.photoUrl,
                            imagePlaceholder = Icon.ALBUM,
                            clickAction = Action.GameClicked(game.id),
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
                val sourceInfo = PdfConfigById(
                    songId = song.id,
                    pageNumber = 0
                )
                ImageNameCaptionListModel(
                    dataId = song.id + ID_PREFIX_SONGS,
                    name = song.name,
                    caption = song.gameName,
                    sourceInfo = sourceInfo,
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id)
                )
            }
        } else {
            emptyList()
        }

        return (composerModel + ctaModels + gameModels + songModels).toImmutableList()
    }

    companion object {
        private const val ID_PREFIX_GAMES = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
