package com.vgleadsheets.remaster.games.detail

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
import kotlinx.collections.immutable.toPersistentList

data class State(
    val game: LCE<Game> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = if (game is LCE.Content) {
        TitleBarModel(
            title = game.data.name,
        )
    } else {
        TitleBarModel()
    }

    @Suppress("LongMethod")
    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val gameModels = gameSection()
        val ctaModels = ctaSection(stringProvider)
        val composerModels = composerSection(stringProvider)
        val songModels = songSection(stringProvider)

        return (gameModels + ctaModels + composerModels + songModels).toPersistentList()
    }

    private fun gameSection() = when (game) {
        is LCE.Content -> {
            val photoUrl = game.data.photoUrl
            if (photoUrl != null) {
                listOf(
                    HeroImageListModel(
                        sourceInfo = photoUrl,
                        imagePlaceholder = Icon.ALBUM,
                        name = null,
                        clickAction = VglsAction.Noop,
                    )
                )
            } else {
                emptyList()
            }
        }

        is LCE.Error -> error(game.operationName, game.error)
        is LCE.Loading -> loading(game.operationName, LoadingType.BIG_IMAGE, 1)
        LCE.Uninitialized -> emptyList()
    }

    @Suppress("MagicNumber")
    private fun ctaSection(stringProvider: StringProvider) = when (game) {
        is LCE.Content -> {
            listOf(
                favoriteCtaItem(stringProvider),
            ).flatten()
        }

        is LCE.Error -> emptyList()
        is LCE.Loading -> loading(game.operationName + ".cta", LoadingType.TEXT_IMAGE, 1)
        LCE.Uninitialized -> emptyList()
    }

    @Suppress("MagicNumber")
    private fun composerSection(stringProvider: StringProvider) = when (composers) {
        is LCE.Content -> {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_GAME)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_GAME.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = composers.data.map { composer ->
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
        }

        is LCE.Error -> error(composers.operationName, composers.error)
        is LCE.Loading -> loading(
            operationName = composers.operationName,
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
                    stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_GAME)
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
        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
