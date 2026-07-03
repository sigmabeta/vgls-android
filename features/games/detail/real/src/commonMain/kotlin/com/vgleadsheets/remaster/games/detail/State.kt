package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.strings.VglsStringId
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
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val game: LCE<Game> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
    val isAvailableOffline: LCE<Boolean> = LCE.Uninitialized,
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
        val gameModels = gameSection(stringProvider)
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

    private fun gameSection(stringProvider: StringProvider) = game.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_GAME,
        loadingType = LoadingType.BIG_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = false,
        content = {
            val photoUrl = data.photoUrl
            if (photoUrl != null) {
                listOf(
                    HeroImageListModel(
                        sourceInfo = SourceInfo(photoUrl),
                        imagePlaceholder = Icon.Album,
                        contentDescription = stringProvider.getString(VglsStringId.ACCY_CDESC_HERO_GAME),
                        clickAction = SageAction.Noop,
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
            listOf(
                offlineCtaItem(stringProvider),
                favoriteCtaItem(stringProvider),
            ).flatten()
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
                    stringProvider.getString(VglsStringId.SECTION_HEADER_COMPOSERS_FROM_GAME)
                ),
                HorizontalScrollerListModel(
                    dataId = VglsStringId.SECTION_HEADER_COMPOSERS_FROM_GAME.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = data.map { composer ->
                        WideItemListModel(
                            dataId = composer.id + ID_PREFIX_COMPOSERS,
                            name = composer.name,
                            sourceInfo = composer.photoUrl,
                            imagePlaceholder = Icon.Person,
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
                    stringProvider.getString(VglsStringId.SECTION_HEADER_SONGS_FROM_GAME)
                )
            ) + data.map { song ->
                val sourceInfo = PdfConfigById(
                    songId = song.id,
                    isAltSelected = false,
                    pageNumber = 0,
                    pdfSize = PdfSize.THUMBNAIL,
                )

                ImageNameListModel(
                    dataId = song.id + ID_PREFIX_SONGS,
                    name = song.name,
                    sourceInfo = SourceInfo(sourceInfo),
                    imagePlaceholder = Icon.Description,
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
                Icon.FavoriteFilled,
                VglsStringId.CTA_FAVORITE_REMOVE,
                Action.RemoveFavoriteClicked,
            )
        } else {
            Triple(
                Icon.FavoriteEmpty,
                VglsStringId.CTA_FAVORITE_ADD,
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
                Icon.OfflineFilled,
                VglsStringId.CTA_OFFLINE_REMOVE,
                Action.DisableOfflineClicked,
            )
        } else {
            Triple(
                Icon.OfflineOutline,
                VglsStringId.CTA_OFFLINE_ADD,
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
        private const val SECTION_NAME_GAME = "section.game"
        private const val SECTION_NAME_CTA = "section.cta"
        private const val SECTION_NAME_COMPOSERS = "section.composer"
        private const val SECTION_NAME_SONGS = "section.song"

        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_SONGS = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
    }
}
