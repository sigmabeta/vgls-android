package com.vgleadsheets.features.main.sheet

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.sheet.SongViewModel.Companion.ID_COMPOSER_MULTIPLE
import com.vgleadsheets.features.main.sheet.SongViewModel.Companion.RATING_MAXIMUM
import com.vgleadsheets.features.main.sheet.SongViewModel.Companion.RATING_MINIMUM
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: SongState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig {
    private val songLoad = state.contentLoad.song

    private val song = songLoad.content()

    private val songAliasesLoad = state.contentLoad.songAliases

    private val songAliases = songAliasesLoad.content()

    private val tagValuesLoad = state.contentLoad.tagValues

    private val tagValues = tagValuesLoad.content()

    override val titleConfig = Title.Config(
        song?.name ?: resources.getString(R.string.unknown_song),
        song?.captionText(),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        Page.generateThumbUrl(
            baseImageUrl,
            hudState.selectedPart.apiId,
            song?.filename ?: ""
        ),
        R.drawable.placeholder_sheet,
        true,
        songLoad.isLoading()
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !tagValues.isNullOrEmpty()
    ) {
        ctaSection() + detailsSection() + akaSection() + tagValuesSection()
    }

    override val emptyConfig = EmptyState.Config(
        false,
        0,
        ""
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        SongFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    private fun akaSection(): List<ListModel> {
        return songAliases?.map {
            LabelValueListModel(
                resources.getString(R.string.label_detail_aka),
                it.name,
                { },
                (it.id ?: 0L) + ID_OFFSET_ALIAS
            )
        } ?: emptyList()
    }

    private fun detailsSection(): List<ListModel> {
        val composer = (song ?: return emptyList())
            .composers
            ?.joinToString(", ") { it.name } ?: resources.getString(R.string.unknown_composer)

        val composerId =
            if (song.composers?.size == 1) song.composers?.first()!!.id else ID_COMPOSER_MULTIPLE

        return listOf(
            LabelValueListModel(
                resources.getString(R.string.label_detail_composer),
                composer,
                { clicks.composer(composerId) },
                composerId + ID_OFFSET_COMPOSER
            ),
            LabelValueListModel(
                resources.getString(R.string.label_detail_game),
                song.gameName,
                { clicks.game(song.gameId) },
                song.gameId + ID_OFFSET_GAME
            )
        )
    }

    private fun ctaSection(): List<ListModel> {
        song ?: return emptyList()

        val spec = PerfSpec.SHEET

        perfTracker.onPartialContentLoad(spec)
        perfTracker.onFullContentLoad(spec)

        return listOf(
            CtaListModel(
                R.drawable.ic_description_24dp,
                resources.getString(R.string.cta_view_sheet)
            ) { clicks.viewSheet(song.id) },
            CtaListModel(
                R.drawable.ic_play_circle_filled_24,
                resources.getString(R.string.cta_youtube)
            ) { clicks.searchYoutube(song.name, song.gameName) }
        )
    }

    private suspend fun tagValuesSection(): List<ListModel> {
        return listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_tags)
            )
        ) + dedupeTagValues(tagValues ?: return emptyList()).mapYielding {
            val valueAsNumber = it.name.toIntOrNull() ?: -1

            return@mapYielding if (valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM) {
                LabelRatingStarListModel(
                    it.tagKeyName,
                    valueAsNumber,
                    { clicks.tagValue(it.id) },
                    it.id + ID_OFFSET_TAG_VALUE
                )
            } else {
                LabelValueListModel(
                    it.tagKeyName,
                    it.name,
                    { clicks.tagValue(it.id) },
                    it.id + ID_OFFSET_TAG_VALUE
                )
            }
        }.sortedBy { it.javaClass.simpleName }
    }

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

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

    private fun Song.captionText() = resources.getString(
        R.string.subtitle_pages,
        if (hudState.selectedPart == Part.VOCAL) {
            lyricPageCount
        } else {
            pageCount
        }
    )

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
        private const val ID_OFFSET_ALIAS = 1_000_000_000L
        private const val ID_OFFSET_TAG_VALUE = 1_000_000_000_000L
    }
}
