package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.images.Page
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

data class State(
    val title: String? = null,
    val sheetUrlInfo: UrlInfo = UrlInfo(),
    val song: Song? = null,
    val composers: List<Composer>? = null,
    val game: Game? = null,
    val songAliases: List<SongAlias> = emptyList(),
    val tagValues: List<TagValue> = emptyList(),
) : ListState() {
    override fun title() = song?.name

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val imageUrl = song?.imageUrl(sheetUrlInfo.imageBaseUrl, sheetUrlInfo.partId)
        val songModel = if (imageUrl != null) {
            listOf<ListModel>(
                HeroImageListModel(
                    imageUrl = imageUrl,
                    imagePlaceholder = Icon.ALBUM,
                    name = song!!.name,
                    clickAction = Action.SongThumbnailClicked(song.id),
                )
            )
        } else {
            emptyList()
        }

        val composerModels = if (composers?.isNotEmpty() == true) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_COMPOSERS_FROM_GAME)
                ),
                HorizontalScrollerListModel(
                    dataId = StringId.SECTION_HEADER_COMPOSERS_FROM_GAME.hashCode() + ID_PREFIX_SCROLLER_CONTENT,
                    scrollingItems = composers.map { composer ->
                        WideItemListModel(
                            dataId = composer.id + ID_PREFIX_COMPOSERS,
                            name = composer.name,
                            imageUrl = composer.photoUrl,
                            imagePlaceholder = Icon.PERSON,
                            clickAction = Action.ComposerClicked(composer.id)
                        )
                    }.toImmutableList()
                )
            )
        } else {
            emptyList()
        }

        val gameModel = if (game != null) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_GAMES_FROM_SONG)
                ),
                HeroImageListModel(
                    imageUrl = game.photoUrl ?: "",
                    imagePlaceholder = Icon.ALBUM,
                    name = game?.name,
                    clickAction = Action.GameClicked(game.id),
                )
            )
        } else {
            emptyList()
        }

        val dedupedTagValues = dedupeTagValues(tagValues)

        val difficultyValues = dedupedTagValues.filter {
            val valueAsNumber = it.name.toIntOrNull() ?: -1
            valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM
        }

        val detailValues = dedupedTagValues.filter {
            val valueAsNumber = it.name.toIntOrNull() ?: -1
            valueAsNumber !in RATING_MINIMUM..RATING_MAXIMUM
        }

        val difficultyModels = if (difficultyValues.isNotEmpty()) {
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

        val aboutModels = if (songAliases.isNotEmpty() || detailValues.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    stringProvider.getString(StringId.SECTION_HEADER_ABOUT_SONG)
                )
            ) + songAliases.map {
                LabelValueListModel(
                    label = stringProvider.getString(StringId.LABEL_SONG_ALSO_KNOWN_AS),
                    value = it.name,
                    clickAction = VglsAction.Noop,
                    dataId = (it.id ?: 0L) + ID_PREFIX_AKA
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

        return (songModel + composerModels + gameModel + difficultyModels + aboutModels).toPersistentList()
    }

    private fun Song.imageUrl(baseImageUrl: String?, selectedPart: String?): String? {
        return Page.generateImageUrl(
            baseImageUrl = baseImageUrl ?: return null,
            partApiId = selectedPart ?: return null,
            filename = filename,
            isAlternateEnabled = isAltSelected,
            pageNumber = 1
        )
    }

    private fun dedupeTagValues(
        tagValues: List<TagValue>
    ): List<TagValue> {
        val dupedTagValueGroups = tagValues
            .groupBy { it.tagKeyName }
            .filter { it.value.size > 1 }

        if (dupedTagValueGroups.isEmpty()) {
            return tagValues
        }

        val tempValues = tagValues.toMutableList()

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
        return tempValues.toList()
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
