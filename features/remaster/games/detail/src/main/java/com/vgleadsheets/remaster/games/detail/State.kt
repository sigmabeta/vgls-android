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
import com.vgleadsheets.list.ColumnType
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.toImmutableList

@Suppress("MagicNumber")
data class State(
    val game: LCE<Game> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override val columnType = ColumnType.Staggered(320, false)

    override fun title(stringProvider: StringProvider) = if (game is LCE.Content) {
        TitleBarModel(
            title = game.data.name,
        )
    } else {
        TitleBarModel()
    }

    @Suppress("LongMethod")
    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val gameModels = gameSection()
        val ctaModels = ctaSection(stringProvider)
        val composerModels = composerSection(stringProvider)
        val songModels = songSection(stringProvider)

        return listOf(
            gameModels,
            ctaModels,
            composerModels,
            songModels
        )
    }

    private fun gameSection() = game.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_GAME,
        loadingType = LoadingType.BIG_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = false,
        content = {
            val photoUrl = data.photoUrl
            if (photoUrl != null) {
                listOf(
                    HeroImageListModel(
                        sourceInfo = photoUrl,
                        imagePlaceholder = Icon.ALBUM,
                        clickAction = VglsAction.Noop,
                    )
                )
            } else {
                emptyList()
            }
        },
    )

    @Suppress("MagicNumber")
    private fun ctaSection(stringProvider: StringProvider) = game.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_CTA,
        loadingItemCount = 0,
        loadingWithHeader = false,
        content = {
            listOf(favoriteCtaItem(stringProvider)).flatten()
        },
    )

    @Suppress("MagicNumber")
    private fun composerSection(stringProvider: StringProvider) = composers.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_COMPOSERS,
        loadingType = LoadingType.WIDE_ITEM,
        loadingWithHeader = true,
        loadingHorizScrollable = true,
        content = {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_GAME)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_GAME.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = data.map { composer ->
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
        },
    )

    private fun songSection(stringProvider: StringProvider) = songs.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_SONGS,
        loadingType = LoadingType.TEXT_IMAGE,
        loadingItemCount = 8,
        loadingWithHeader = true,
        content = {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_GAME)
                )
            ) + data.map { song ->
                val sourceInfo = PdfConfigById(
                    songId = song.id,
                    isAltSelected = false,
                    pageNumber = 0,
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
    )

    @Suppress("MagicNumber")
    private fun favoriteCtaItem(
        stringProvider: StringProvider
    ) = isFavorite.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = false
    ) {
        val (icon, label, action) = if (data) {
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

    companion object {
        private const val SECTION_NAME_GAME = "section.game"
        private const val SECTION_NAME_CTA = "section.cta"
        private const val SECTION_NAME_COMPOSERS = "section.composer"
        private const val SECTION_NAME_SONGS = "section.song"

        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
