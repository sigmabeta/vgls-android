package com.vgleadsheets.features.main.tagsongs.better

import android.content.res.Resources
import com.vgleadsheets.components.ImageNameCaptionListModel
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
import com.vgleadsheets.features.main.tagsongs.BuildConfig
import com.vgleadsheets.features.main.tagsongs.R
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.thumbUrl
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterTagValueSongConfig(
    private val state: BetterTagValueSongState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterTagValueSongViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterTagValueSongState, BetterTagValueSongClicks> {
    private val tagValueLoad = state.contentLoad.tagValue

    private val tagValue = tagValueLoad.content()

    private val songsLoad = state.contentLoad.songs

    private val songs = songsLoad.content()

    override val titleConfig = Title.Config(
        resources.getString(
            R.string.title_tag_value_songs,
            tagValue?.tagKeyName ?: resources.getString(R.string.unknown_tag_key),
            tagValue?.name ?: resources.getString(R.string.unknown_tag_value)
        ),
        songs?.captionText(),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        shouldShow = true,
        isLoading = tagValueLoad.isLoading()
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !songs.isNullOrEmpty()
    ) {
        songs?.filteredForVocals(hudState.selectedPart.apiId)
            ?.map {
                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    it.captionText(),
                    it.thumbUrl(baseImageUrl, hudState.selectedPart),
                    R.drawable.placeholder_sheet,
                    onSongClicked()
                )
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        songs?.isEmpty() == true,
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_tag_value_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterTagValueSongFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun onSongClicked() =
        object : ImageNameCaptionListModel.EventHandler {
            override fun onClicked(clicked: ImageNameCaptionListModel) {
                viewModel.onSongClicked(
                    clicked.dataId,
                    clicked.name,
                    songsLoad
                        .content()
                        ?.firstOrNull { it.id == clicked.dataId }
                        ?.gameName
                        ?: "",
                    hudState.selectedPart.apiId
                )
            }

            override fun clearClicked() {}
        }

    private fun Song.captionText() = gameName

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}
