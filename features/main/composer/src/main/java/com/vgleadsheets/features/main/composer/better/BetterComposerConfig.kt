package com.vgleadsheets.features.main.composer.better

import android.content.res.Resources
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.features.main.composer.BuildConfig
import com.vgleadsheets.features.main.composer.R
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
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.thumbUrl
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterComposerConfig(
    private val state: BetterComposerState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterComposerViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterComposerState, BetterComposerClicks> {
    private val composerLoad = state.contentLoad.composer

    private val composer = composerLoad.content()

    private val songsLoad = state.contentLoad.songs

    private val songs = songsLoad.content()

    override val titleConfig = Title.Config(
        composer?.name ?: resources.getString(R.string.unknown_composer),
        songs?.captionText(),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        composer?.photoUrl,
        R.drawable.placeholder_composer,
        true,
        composerLoad.isLoading()
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !songs.isNullOrEmpty()
    ) {
        songs?.filteredForVocals(hudState.selectedPart.apiId)
            ?.map { song ->
                ImageNameCaptionListModel(
                    song.id,
                    song.name,
                    song.captionText(),
                    song.thumbUrl(baseImageUrl, hudState.selectedPart),
                    R.drawable.placeholder_sheet
                ) {
                    viewModel.onSongClicked(
                        song.id,
                        song.name,
                        song.gameName,
                        hudState.selectedPart.apiId
                    )
                }
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        songs?.isEmpty() == true,
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_composer_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterComposerFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun Song.captionText() = gameName

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}
