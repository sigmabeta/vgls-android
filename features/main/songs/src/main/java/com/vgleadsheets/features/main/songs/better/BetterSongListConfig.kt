package com.vgleadsheets.features.main.songs.better

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.songs.BuildConfig
import com.vgleadsheets.features.main.songs.R
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.thumbUrl
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterSongListConfig(
    private val state: BetterSongListState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterSongListViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterSongListState, BetterSongListClicks> {
    private val songsLoad = state.contentLoad.songsLoad

    private val songs = songsLoad.content()

    override val titleConfig = Title.Config(
        resources.getString(R.string.app_name),
        resources.getString(R.string.subtitle_all_sheets),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { }
    )

    override val contentConfig = Content.Config(
        !state.contentLoad.isNullOrEmpty()
    ) {
        songsLoad.content()
            ?.filteredForVocals(hudState.selectedPart.apiId)
            ?.map {
                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    it.captionText(),
                    it.thumbUrl(baseImageUrl, hudState.selectedPart),
                    R.drawable.placeholder_sheet,
                    onSongClicked(songsLoad)
                )
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterSongListFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun Song.captionText() = gameName

    private fun onSongClicked(songsLoad: Async<List<Song>>) =
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
}
