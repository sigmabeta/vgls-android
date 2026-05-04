package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain.Companion.LOAD_OPERATION_SONG
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.CtaListModel
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.LabelRatingStarListModel
import net.sigmabeta.sage.components.LabelValueListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SectionHeaderListModel
import net.sigmabeta.sage.components.SheetPageCardListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.components.SinglePageListModel
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
    val sheetUrlInfo: LCE<UrlInfo> = LCE.Uninitialized,
    val song: LCE<Song> = LCE.Uninitialized,
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
    val game: LCE<Game> = LCE.Uninitialized,
    val songAliases: LCE<List<SongAlias>> = LCE.Uninitialized,
    val tagValues: LCE<List<TagValue>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
    val isAvailableOffline: LCE<Boolean> = LCE.Uninitialized,
    val isAltSelected: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override val columnType = ColumnType.Staggered(320, true)

    override fun title(stringProvider: StringProvider): TitleBarModel = if (song is LCE.Content) {
            val gameName = song.data.gameName
            TitleBarModel(
                title = song.data.name,
                subtitle = gameName.let { stringProvider.getStringOneArg(VglsStringId.SCREEN_SUBTITLE_SONG_DETAIL, it) },
            )
        } else {
            TitleBarModel()
        }

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val sheetPreviewSection = sheetPreviewSection()
        val ctaSection = ctaSection(stringProvider)
        val composerModels = composerSection(stringProvider)
        val gameModel = gameSection(stringProvider)

        val dedupedTagValues = dedupeTagValues(tagValues)

        val difficultySection = difficultySection(dedupedTagValues, stringProvider)

        val aboutSection = aboutSection(dedupedTagValues, stringProvider)

        return listOf(
            sheetPreviewSection,
            ctaSection,
            composerModels,
            gameModel,
            difficultySection,
            aboutSection,
        )
    }

    private fun sheetPreviewSection() = song.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_SONG,
        dontUnroll = true,
        loadingType = LoadingType.PAGE,
        loadingItemCount = 2,
        loadingWithHeader = false,
        loadingHorizScrollable = true,
        columns = ListModel.COLUMNS_ALL,
    ) {
        if (sheetUrlInfo !is LCE.Content) {
            return@sectionWithStandardErrorAndLoading sheetLoading()
        }
        if (isAltSelected !is LCE.Content) {
            return@sectionWithStandardErrorAndLoading sheetLoading()
        }

        val selectedPart = sheetUrlInfo.data.partId ?: return@sectionWithStandardErrorAndLoading sheetLoading()
        val altSelection = isAltSelected.data
        val pageCount = data.pageCount(selectedPart, altSelection)

        val actualPartApiId = if (pageCount > 0) {
            selectedPart
        } else {
            Part.C.apiId
        }

        val showLyricsMissingWarning = selectedPart == Part.VOCAL.apiId && actualPartApiId != Part.VOCAL.apiId
        val actualPageCount = data.pageCount(actualPartApiId, isAltSelected.data)

        listOf(
            if (actualPageCount > 1) {
                HorizontalScrollerListModel(
                    dataId = data.id,
                    scrollingItems = List(actualPageCount) { pageNumber ->
                        sheetPage(
                            data,
                            pageNumber,
                            showLyricsMissingWarning,
                            altSelection,
                            PdfSize.LARGE
                        )
                    }.toImmutableList()
                )
            } else {
                singlePage(data, false, altSelection)
            }
        )
    }

    private fun sheetLoading() = loading(LOAD_OPERATION_SONG, LoadingType.PAGE, 1)

    private fun singlePage(
        song: Song,
        showLyricsMissingWarning: Boolean,
        altSelectionValue: Boolean,
    ) = SinglePageListModel(
        sheetPageCardModel = sheetPage(
            song = song,
            pageNumber = 0,
            showLyricsMissingWarning = showLyricsMissingWarning,
            altSelectionValue = altSelectionValue,
            pdfSize = PdfSize.LARGE,
        )
    )

    private fun sheetPage(
        song: Song,
        pageNumber: Int,
        showLyricsMissingWarning: Boolean,
        altSelectionValue: Boolean,
        pdfSize: PdfSize,
    ) = SheetPageCardListModel(
        SheetPageListModel(
            title = song.name,
            gameName = song.gameName,
            composers = song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
            pageNumber = pageNumber,
            clickAction = Action.SongThumbnailClicked(song.id, pageNumber),
            showLyricsWarning = showLyricsMissingWarning,
            pdfConfigById = PdfConfigById(
                songId = song.id,
                pageNumber = pageNumber,
                isAltSelected = altSelectionValue,
                pdfSize = pdfSize,
            ),
        )
    )

    @Suppress("MagicNumber")
    private fun ctaSection(stringProvider: StringProvider) = song.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_CTA,
        loadingItemCount = 0
    ) {
        listOf(
            offlineCtaItem(stringProvider),
            favoriteCtaItem(stringProvider),
            altSelectionCtaItem(stringProvider),
            searchYoutubeItem(stringProvider),
        ).flatten()
    }

    private fun gameSection(stringProvider: StringProvider) = game.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_GAMES,
        loadingType = LoadingType.BIG_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = true,
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(VglsStringId.SECTION_HEADER_GAMES_FROM_SONG)
            ),
            HeroImageListModel(
                sourceInfo = SourceInfo(data.photoUrl),
                imagePlaceholder = Icon.ALBUM,
                contentDescription = data.name,
                clickAction = Action.GameClicked(data.id),
            )
        )
    }

    @Suppress("MagicNumber")
    private fun composerSection(stringProvider: StringProvider) = composers.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_COMPOSERS,
        loadingType = LoadingType.WIDE_ITEM,
        loadingHorizScrollable = true
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(VglsStringId.SECTION_HEADER_COMPOSERS_FROM_SONG)
            ),
            HorizontalScrollerListModel(
                dataId = VglsStringId.SECTION_HEADER_COMPOSERS_FROM_SONG.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
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
    }

    @Suppress("MagicNumber")
    private fun difficultySection(
        dedupedTagValues: LCE<List<TagValue>>,
        stringProvider: StringProvider
    ) = dedupedTagValues.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_DIFFICULTY,
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 3,
    ) {
        val difficultyValues = data
            .filter { it.isDifficultyValue() }
            .sortedBy { it.tagKeyName }
            .sortedBy { it.tagKeyName.split(" ").size }

        if (difficultyValues.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(VglsStringId.SECTION_HEADER_DIFFICULTY_FOR_SONG)
                )
            ) + difficultyValues.map { difficultyValue ->
                LabelRatingStarListModel(
                    label = difficultyValue.tagKeyName,
                    value = difficultyValue.name.toIntOrNull() ?: -1,
                    clickAction = Action.TagValueClicked(difficultyValue.id),
                    dataId = difficultyValue.id + ID_PREFIX_TAG_VALUE
                )
            }
        } else {
            emptyList()
        }
    }

    @Suppress("MagicNumber")
    private fun aboutSection(
        dedupedTagValues: LCE<List<TagValue>>,
        stringProvider: StringProvider
    ) = dedupedTagValues.sectionWithStandardErrorAndLoading(
        sectionName = SECTION_NAME_ABOUT,
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 3,
        loadingWithHeader = true,
    ) {
        val detailValues = data.filter {
            !it.isDifficultyValue()
        }

        val aliasValues = if (songAliases is LCE.Content) {
            songAliases.data
        } else {
            emptyList()
        }

        if (aliasValues.isNotEmpty() || detailValues.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(VglsStringId.SECTION_HEADER_ABOUT_SONG)
                )
            ) + aliasValues.map { alias ->
                LabelValueListModel(
                    label = stringProvider.getString(VglsStringId.LABEL_SONG_ALSO_KNOWN_AS),
                    value = alias.name,
                    clickAction = SageAction.Noop,
                    dataId = (alias.id ?: 0L) + ID_PREFIX_AKA,
                )
            } + detailValues.map { detailValue ->
                LabelValueListModel(
                    label = detailValue.tagKeyName,
                    value = detailValue.name,
                    clickAction = Action.TagValueClicked(detailValue.id),
                    dataId = detailValue.id + ID_PREFIX_TAG_VALUE
                )
            }
        } else {
            emptyList()
        }
    }

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
                Icon.FAVORITE_FILLED,
                VglsStringId.CTA_FAVORITE_REMOVE,
                Action.RemoveFavoriteClicked,
            )
        } else {
            Triple(
                Icon.FAVORITE_EMPTY,
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
                Icon.OFFLINE_FILLED,
                VglsStringId.CTA_OFFLINE_REMOVE,
                Action.DisableOfflineClicked,
            )
        } else {
            Triple(
                Icon.OFFLINE_OUTLINE,
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

    @Suppress("ReturnCount")
    private fun altSelectionCtaItem(stringProvider: StringProvider): List<ListModel> {
        val song = (song as? LCE.Content)?.data ?: return emptyList()

        if (song.altPageCount > 0) {
            return isAltSelected.withStandardErrorAndLoading(
                loadingType = LoadingType.TEXT_IMAGE,
                loadingItemCount = 1,
                loadingWithHeader = false
            ) {
                val isAltSelected = data

                val label = if (isAltSelected) {
                    VglsStringId.CTA_ALT_UNSELECT
                } else {
                    VglsStringId.CTA_ALT_SELECT
                }

                listOf(
                    CtaListModel(
                        icon = Icon.DESCRIPTION,
                        name = stringProvider.getString(label),
                        clickAction = Action.ToggleAltSelectedClicked,
                    )
                )
            }
        }

        return emptyList()
    }

    private fun searchYoutubeItem(stringProvider: StringProvider): List<CtaListModel> = listOf(
            CtaListModel(
                icon = Icon.SEARCH_YOUTUBE,
                name = stringProvider.getString(VglsStringId.CTA_SEARCH_YOUTUBE),
                clickAction = Action.SearchYoutubeClicked,
            )
        )

    @Suppress("ReturnCount")
    private fun dedupeTagValues(
        tagValues: LCE<List<TagValue>>
    ): LCE<List<TagValue>> {
        if (tagValues !is LCE.Content) {
            return tagValues
        }

        val dupedTagValueGroups = tagValues
            .data
            .groupBy { it.tagKeyName }
            .filter { it.value.size > 1 }

        if (dupedTagValueGroups.isEmpty()) {
            return tagValues
        }

        val tempValues = tagValues.data.toMutableList()

        dupedTagValueGroups.forEach { entry ->
            val dupesWithThisKey = entry.value
            tempValues.removeAll(dupesWithThisKey)

            val renamedDupesWithThisKey = dupesWithThisKey
                .mapIndexed { index, originalValue ->
                    TagValue(
                        originalValue.id,
                        originalValue.name,
                        originalValue.tagKeyId,
                        "${originalValue.tagKeyName} ${index + 1}",
                        originalValue.songs
                    )
                }

            tempValues.addAll(renamedDupesWithThisKey)
        }

        tempValues.sortBy { it.tagKeyName }
        return LCE.Content(tempValues.toList())
    }

    companion object {
        private const val SECTION_NAME_SONG = "section.song"
        private const val SECTION_NAME_CTA = "section.cta"
        private const val SECTION_NAME_COMPOSERS = "section.composer"
        private const val SECTION_NAME_GAMES = "section.game"
        private const val SECTION_NAME_DIFFICULTY = "section.difficulty"
        private const val SECTION_NAME_ABOUT = "section.about"

        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_TAG_VALUE = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
        private const val ID_PREFIX_AKA = 1_000_000_000_000_000L
    }
}
