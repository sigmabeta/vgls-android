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
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

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

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
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
            ).toPersistentList()
    }

    private fun sheetPreviewSection(): List<ListModel> {
        return when (song) {
            is LCE.Content -> sheetPreviewContent(song.data)
            is LCE.Error -> error(song.operationName, song.error)
            is LCE.Loading -> loading(song.operationName, LoadingType.SHEET, 1)
            LCE.Uninitialized -> emptyList()
        }
    }

    @Suppress("ReturnCount")
    private fun sheetPreviewContent(song: Song): List<ListModel> {
        if (sheetUrlInfo !is LCE.Content) {
            return loading(LOAD_OPERATION_SONG, LoadingType.SHEET, 1)
        }

        val selectedPart = sheetUrlInfo.data.partId ?: return loading(LOAD_OPERATION_SONG, LoadingType.SHEET, 1)
        val pageCount = song.pageCount(selectedPart)

        return listOf(
            if (pageCount > 1) {
                HorizontalScrollerListModel(
                    dataId = song.id,
                    scrollingItems = List(pageCount) { pageNumber ->
                        sheetPage(
                            song,
                            pageNumber
                        )
                    }.toImmutableList()
                )
            } else {
                sheetPage(song, 0)
            }
        )
    }

    private fun sheetPage(
        song: Song,
        pageNumber: Int
    ) = SheetPageCardListModel(
        SheetPageListModel(
            title = song.name,
            gameName = song.gameName,
            composers = song.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
            pageNumber = pageNumber,
            clickAction = Action.SongThumbnailClicked(song.id, pageNumber),
            sourceInfo = PdfConfigById(
                songId = song.id,
                pageNumber = pageNumber
            ),
        )
    )

    @Suppress("MagicNumber")
    private fun ctaSection(stringProvider: StringProvider) = when (song) {
        is LCE.Content -> {
            listOf(
                favoriteCtaItem(stringProvider),
                searchYoutubeItem(stringProvider),
            ).flatten()
        }

        is LCE.Error -> emptyList()
        is LCE.Loading -> loading(song.operationName + ".cta", LoadingType.TEXT_IMAGE, 2)
        LCE.Uninitialized -> emptyList()
    }

    private fun gameSection(stringProvider: StringProvider) = when (game) {
        is LCE.Content -> {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_SONG)
                ),
                HeroImageListModel(
                    sourceInfo = game.data.photoUrl ?: "",
                    imagePlaceholder = Icon.ALBUM,
                    name = game.data.name,
                    clickAction = Action.GameClicked(game.data.id),
                )
            )
        }

        is LCE.Error -> error(game.operationName, game.error)
        is LCE.Loading -> loading(
            operationName = game.operationName,
            loadingType = LoadingType.BIG_IMAGE,
            loadingItemCount = 1,
            loadingWithHeader = true,
        )
        LCE.Uninitialized -> emptyList()
    }

    @Suppress("MagicNumber")
    private fun composerSection(stringProvider: StringProvider) = when (composers) {
        is LCE.Content -> {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_SONG)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_SONG.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
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

    @Suppress("MagicNumber")
    private fun difficultySection(
        dedupedTagValues: LCE<List<TagValue>>,
        stringProvider: StringProvider
    ) = when (dedupedTagValues) {
        is LCE.Content -> {
            val difficultyValues = dedupedTagValues.data.filter {
                val valueAsNumber = it.name.toIntOrNull() ?: -1
                valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM
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

        is LCE.Error -> error(dedupedTagValues.operationName + ".difficulty", dedupedTagValues.error)
        is LCE.Loading -> loading(
            operationName = dedupedTagValues.operationName + ".difficulty",
            loadingType = LoadingType.SINGLE_TEXT,
            loadingItemCount = 3,
            loadingWithHeader = true,
        )
        LCE.Uninitialized -> emptyList()
    }

    @Suppress("MagicNumber")
    private fun aboutSection(
        dedupedTagValues: LCE<List<TagValue>>,
        stringProvider: StringProvider
    ) = when (dedupedTagValues) {
        is LCE.Content -> {
            val detailValues = dedupedTagValues.data.filter {
                val valueAsNumber = it.name.toIntOrNull() ?: -1
                valueAsNumber !in RATING_MINIMUM..RATING_MAXIMUM
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

        is LCE.Error -> error(dedupedTagValues.operationName + ".details", dedupedTagValues.error)
        is LCE.Loading -> loading(
            operationName = dedupedTagValues.operationName + ".details",
            loadingType = LoadingType.SINGLE_TEXT,
            loadingItemCount = 3,
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

        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5
    }
}
