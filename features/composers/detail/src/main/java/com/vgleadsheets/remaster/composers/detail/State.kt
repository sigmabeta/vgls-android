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
import com.vgleadsheets.images.SourceInfo
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
    val title: LCE<String> = LCE.Uninitialized,
    val composer: LCE<Composer> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val games: LCE<List<Game>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override val columnType = ColumnType.Staggered(320, false)

    override fun title(stringProvider: StringProvider) = if (composer is LCE.Content) {
        TitleBarModel(
            title = composer.data.name,
        )
    } else {
        TitleBarModel()
    }

    @Suppress("LongMethod")
    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val composerModel = composerSection(stringProvider)
        val ctaModels = ctaSection(stringProvider)
        val gameModels = gameSection(stringProvider)
        val songModels = songSection(stringProvider)

        return listOf(
            composerModel,
            ctaModels,
            gameModels,
            songModels
        )
    }

    private fun composerSection(stringProvider: StringProvider) = composer.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_COMPOSER,
        loadingType = LoadingType.BIG_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = false,
    ) {
        val photoUrl = data.photoUrl
        if (photoUrl != null) {
            listOf(
                HeroImageListModel(
                    sourceInfo = SourceInfo(photoUrl),
                    imagePlaceholder = Icon.PERSON,
                    contentDescription = stringProvider.getString(StringId.ACCY_CDESC_HERO_COMPOSER),
                    clickAction = VglsAction.Noop,
                )
            )
        } else {
            emptyList()
        }
    }

    private fun State.ctaSection(stringProvider: StringProvider) = composer.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_CTA,
        loadingItemCount = 0,
        loadingWithHeader = false,
    ) {
        listOf(
            favoriteCtaItem(stringProvider),
        ).flatten()
    }

    private fun gameSection(stringProvider: StringProvider) = games.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_GAMES,
        loadingType = LoadingType.WIDE_ITEM,
        loadingHorizScrollable = true
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_COMPOSER)
            ),
            HorizontalScrollerListModel(
                dataId = StringId.SECTION_HEADER_GAMES_FROM_COMPOSER.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                scrollingItems = data.map { game ->
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

    private fun songSection(stringProvider: StringProvider) = songs.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_SONGS,
        loadingType = LoadingType.TEXT_IMAGE,
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SONGS_FROM_COMPOSER)
            )
        ) + data.map { song ->
            val sourceInfo = SourceInfo(
                PdfConfigById(
                    songId = song.id,
                    pageNumber = 0,
                    isAltSelected = false,
                )
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

    @Suppress("MagicNumber")
    private fun favoriteCtaItem(
        stringProvider: StringProvider
    ) = isFavorite.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_IMAGE,
        loadingWithHeader = false,
        loadingItemCount = 1
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
        private const val SECTION_NAME_COMPOSER = "section.composer"
        private const val SECTION_NAME_GAMES = "section.game"
        private const val SECTION_NAME_CTA = "section.cta"
        private const val SECTION_NAME_SONGS = "section.song"

        private const val ID_PREFIX_GAMES = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
