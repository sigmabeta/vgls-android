package com.vgleadsheets.features.main.sheet.better

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
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.sheet.BuildConfig
import com.vgleadsheets.features.main.sheet.R
import com.vgleadsheets.features.main.sheet.better.BetterSongViewModel.Companion.ID_COMPOSER_MULTIPLE
import com.vgleadsheets.features.main.sheet.better.BetterSongViewModel.Companion.RATING_MAXIMUM
import com.vgleadsheets.features.main.sheet.better.BetterSongViewModel.Companion.RATING_MINIMUM
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.thumbUrl
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterSongConfig(
    private val state: BetterSongState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterSongViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterSongState, BetterSongClicks> {
    private val songLoad = state.contentLoad.song

    private val song = songLoad.content()

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
        song?.thumbUrl(baseImageUrl, hudState.selectedPart),
        R.drawable.placeholder_sheet,
        true,
        songLoad.isLoading()
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !tagValues.isNullOrEmpty()
    ) {
        ctaSection() + detailsSection() + tagValuesSection()
    }

    override val emptyConfig = EmptyState.Config(
        false,
        0,
        ""
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterSongFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

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
                { viewModel.onComposerClicked(composerId, composer) },
                composerId
            ),
            LabelValueListModel(
                resources.getString(R.string.label_detail_game),
                song.gameName,
                { viewModel.onGameClicked(song.gameId, song.gameName) },
                song.gameId
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
            ) {
                viewModel.onCtaClicked(
                    R.drawable.ic_description_24dp,
                    song,
                    hudState.selectedPart
                )
            },
            CtaListModel(
                R.drawable.ic_play_circle_filled_24,
                resources.getString(R.string.cta_youtube)
            ) {
                viewModel.onCtaClicked(
                    R.drawable.ic_play_circle_filled_24,
                    song,
                    hudState.selectedPart
                )
            }
        )
    }

    private fun tagValuesSection(): List<ListModel> {
        return listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_tags)
            )
        ) + dedupeTagValues(tagValues ?: return emptyList()).map {
            val valueAsNumber = it.name.toIntOrNull() ?: -1

            return@map if (valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM) {
                LabelRatingStarListModel(
                    it.tagKeyName,
                    valueAsNumber,
                    { viewModel.onTagValueClicked(it.id) },
                    it.id
                )
            } else {
                LabelValueListModel(
                    it.tagKeyName,
                    it.name,
                    { viewModel.onTagValueClicked(it.id) },
                    it.id
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
}
