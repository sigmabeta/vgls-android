package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val title: LCE<String> = LCE.Uninitialized,
    val composer: LCE<Composer> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val games: LCE<List<Game>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = if (composer is LCE.Content) {
        TitleBarModel(
            title = composer.data.name,
        )
    } else {
        TitleBarModel()
    }

    @Suppress("LongMethod")
    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val composerModel = composerSection()
        val ctaModels = ctaSection(stringProvider)
        val gameModels = gameSection(stringProvider)
        val songModels = songSection(stringProvider)

        return (composerModel + ctaModels + gameModels + songModels).toImmutableList()
    }

    private fun composerSection() = when (composer) {
        is LCE.Content -> {
            val photoUrl = composer.data.photoUrl
            if (photoUrl != null) {
                listOf(
                    HeroImageListModel(
                        sourceInfo = photoUrl,
                        imagePlaceholder = Icon.PERSON,
                        name = null,
                        clickAction = VglsAction.Noop,
                    )
                )
            } else {
                emptyList()
            }
        }

        is LCE.Error -> error(composer.operationName, composer.error)
        is LCE.Loading -> loading(composer.operationName, LoadingType.BIG_IMAGE, 1)
        LCE.Uninitialized -> emptyList()
    }

    private fun State.ctaSection(stringProvider: StringProvider) = when (composer) {
        is LCE.Content -> {
            listOf(
                favoriteCtaItem(stringProvider),
            ).flatten()
        }

        is LCE.Error -> emptyList()
        is LCE.Loading -> loading(composer.operationName + ".cta", LoadingType.TEXT_IMAGE, 1)
        LCE.Uninitialized -> emptyList()
    }

    private fun gameSection(stringProvider: StringProvider) = when (games) {
        is LCE.Content -> {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_COMPOSER)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_GAMES_FROM_COMPOSER.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = games.data.map { game ->
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
        }

        is LCE.Error -> error(games.operationName, games.error)
        is LCE.Loading -> loading(
            operationName = games.operationName,
            loadingType = LoadingType.WIDE_ITEM,
            loadingItemCount = 3,
            loadingWithHeader = true,
            loadingHorizScrollable = true
        )

        LCE.Uninitialized -> emptyList()
    }

    private fun songSection(stringProvider: StringProvider) = when (songs) {
        is LCE.Content -> {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_COMPOSER)
                )
            ) + songs.data.map { song ->
                val sourceInfo = PdfConfigById(
                    songId = song.id,
                    pageNumber = 0
                )

                ImageNameListModel(
                    dataId = song.id + ID_PREFIX_SONGS,
                    name = song.name,
                    sourceInfo = sourceInfo,
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id)
                )
            }
        }

        is LCE.Error -> error(songs.operationName, songs.error)
        is LCE.Loading -> loading(
            operationName = songs.operationName,
            loadingType = LoadingType.TEXT_IMAGE,
            loadingItemCount = 8,
            loadingWithHeader = true,
        )

        LCE.Uninitialized -> emptyList()
    }

    @Suppress("MagicNumber")
    private fun favoriteCtaItem(
        stringProvider: StringProvider
    ) = when (isFavorite) {
        is LCE.Content -> {
            val (icon, label, action) = if (isFavorite.data) {
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
        }

        is LCE.Error -> error(isFavorite.operationName, isFavorite.error)
        is LCE.Loading -> loading(isFavorite.operationName, LoadingType.SINGLE_TEXT, 3)
        LCE.Uninitialized -> emptyList()
    }

    companion object {
        private const val ID_PREFIX_GAMES = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
