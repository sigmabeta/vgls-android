package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.CtaListModel
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SectionHeaderListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.components.WideItemListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val title: LCE<String> = LCE.Uninitialized,
    val composer: LCE<Composer> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val games: LCE<List<Game>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
    val isAvailableOffline: LCE<Boolean> = LCE.Uninitialized,
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
                    clickAction = SageAction.Noop,
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
            offlineCtaItem(stringProvider),
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
                    pdfSize = PdfSize.THUMBNAIL,
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

    @Suppress("MagicNumber")
    private fun offlineCtaItem(
        stringProvider: StringProvider
    ) = isAvailableOffline.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = false
    ) {
        val (icon, label, action) = if (data) {
            Triple(
                Icon.OFFLINE_FILLED,
                StringId.CTA_OFFLINE_REMOVE,
                Action.DisableOfflineClicked,
            )
        } else {
            Triple(
                Icon.OFFLINE_OUTLINE,
                StringId.CTA_OFFLINE_ADD,
                Action.EnableOfflineClicked,
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
