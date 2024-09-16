package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain.Companion.LOAD_OPERATION_SONG
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain.Companion.LOAD_OPERATION_TAG_VALUES
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val sheetUrlInfo: LCE<UrlInfo> = LCE.Uninitialized,
    val song: LCE<Song> = LCE.Uninitialized,
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
    val game: LCE<Game> = LCE.Uninitialized,
    val songAliases: LCE<List<SongAlias>> = LCE.Uninitialized,
    val tagValues: LCE<List<TagValue>> = LCE.Uninitialized,
    val isFavorite: LCE<Boolean> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider): TitleBarModel {
        return if (song is LCE.Content) {
            val gameName = song.data.gameName
            TitleBarModel(
                title = song.data.name,
                subtitle = gameName.let { stringProvider.getStringOneArg(StringId.SCREEN_SUBTITLE_SONG_DETAIL, it) },
            )
        } else {
            TitleBarModel()
        }
    }

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val sheetPreviewSection = sheetPreviewSection()
        val ctaSection = ctaSection(stringProvider)
        val composerModels = composerSection(stringProvider)
        val gameModel = gameSection(stringProvider)

        val dedupedTagValues = dedupeTagValues(tagValues)

        val difficultySection = difficultySection(dedupedTagValues, stringProvider)

        val aboutSection = aboutSection(dedupedTagValues, stringProvider)

        return (
            sheetPreviewSection +
                ctaSection +
                composerModels +
                gameModel +
                difficultySection +
                aboutSection
            )
    }

    private fun sheetPreviewSection() = song.withStandardErrorAndLoading(
        loadingType = LoadingType.SHEET,
        loadingItemCount = 2,
        loadingWithHeader = false,
        loadingHorizScrollable = true,
    ) {
        if (sheetUrlInfo !is LCE.Content) {
            return@withStandardErrorAndLoading sheetLoading()
        }

        val selectedPart = sheetUrlInfo.data.partId ?: return@withStandardErrorAndLoading sheetLoading()
        val pageCount = data.pageCount(selectedPart)

        listOf(
            when {
                pageCount > 1 -> HorizontalScrollerListModel(
                        dataId = data.id,
                        scrollingItems = List(pageCount) { pageNumber ->
                            sheetPage(
                                data,
                                pageNumber,
                                false
                            )
                        }.toImmutableList()
                    )
                pageCount == 0 -> sheetPage(data, 0, true)
                else -> sheetPage(data, 0, false)
            }
        )
    }

    private fun sheetLoading() = loading(LOAD_OPERATION_SONG, LoadingType.SHEET, 1)

    private fun sheetPage(
        song: Song,
        pageNumber: Int,
        showLyricsMissingWarning: Boolean,
    ) = SheetPageCardListModel(
        SheetPageListModel(
            title = song.name,
            gameName = song.gameName,
            composers = song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
            pageNumber = pageNumber,
            clickAction = Action.SongThumbnailClicked(song.id, pageNumber),
            showLyricsWarning = showLyricsMissingWarning,
            sourceInfo = PdfConfigById(
                songId = song.id,
                pageNumber = pageNumber
            ),
        )
    )

    @Suppress("MagicNumber")
    private fun ctaSection(stringProvider: StringProvider) = song.withStandardErrorAndLoading(
        loadingItemCount = 0
    ) {
        listOf(
            favoriteCtaItem(stringProvider),
            searchYoutubeItem(stringProvider),
        ).flatten()
    }

    private fun gameSection(stringProvider: StringProvider) = game.withStandardErrorAndLoading(
        loadingType = LoadingType.BIG_IMAGE,
        loadingItemCount = 1,
        loadingWithHeader = true,
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_SONG)
            ),
            HeroImageListModel(
                sourceInfo = data.photoUrl ?: "",
                imagePlaceholder = Icon.ALBUM,
                name = data.name,
                clickAction = Action.GameClicked(data.id),
            )
        )
    }

    @Suppress("MagicNumber")
    private fun composerSection(stringProvider: StringProvider) = composers.withStandardErrorAndLoading(
        loadingType = LoadingType.WIDE_ITEM,
        loadingHorizScrollable = true
    ) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_SONG)
            ),
            HorizontalScrollerListModel(
                dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_SONG.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
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
    ) = dedupedTagValues.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 3,
        loadingOperationNameOverride = "$LOAD_OPERATION_TAG_VALUES.difficulty",
    ) {
        val difficultyValues = data.filter {
            it.isDifficultyValue()
        }

        if (difficultyValues.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_DIFFICULTY_FOR_SONG)
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
    ) = dedupedTagValues.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 3,
        loadingWithHeader = true,
        loadingOperationNameOverride = "$LOAD_OPERATION_TAG_VALUES.details",
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
                    stringProvider.getString(StringId.SECTION_HEADER_ABOUT_SONG)
                )
            ) + aliasValues.map { alias ->
                LabelValueListModel(
                    label = stringProvider.getString(StringId.LABEL_SONG_ALSO_KNOWN_AS),
                    value = alias.name,
                    clickAction = VglsAction.Noop,
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

    private fun searchYoutubeItem(stringProvider: StringProvider): List<CtaListModel> {
        return listOf(
            CtaListModel(
                icon = Icon.SEARCH_YOUTUBE,
                name = stringProvider.getString(StringId.CTA_SEARCH_YOUTUBE),
                clickAction = Action.SearchYoutubeClicked,
            )
        )
    }

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
        private const val ID_PREFIX_COMPOSERS = 1_000_000L
        private const val ID_PREFIX_TAG_VALUE = 1_000_000_000L
        private const val ID_PREFIX_SCROLLER_CONTENT = 1_000_000_000_000L
        private const val ID_PREFIX_AKA = 1_000_000_000_000_000L
    }
}
