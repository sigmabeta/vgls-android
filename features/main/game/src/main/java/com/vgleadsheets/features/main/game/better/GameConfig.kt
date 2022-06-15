package com.vgleadsheets.features.main.game.better

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.features.main.game.BuildConfig
import com.vgleadsheets.features.main.game.R
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
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class GameConfig(
    private val state: BetterGameState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterGameViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterGameState, GameClicks> {
    private val gameLoad = state.contentLoad.game

    private val game = gameLoad.content()

    private val songsLoad = state.contentLoad.songs

    private val songs = songsLoad.content()

    override val titleConfig = Title.Config(
        game?.name ?: resources.getString(R.string.unknown_game),
        songs?.captionText(),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        game?.photoUrl,
        R.drawable.placeholder_game,
        true,
        gameLoad.isLoading()
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
                    it.subtitleText(),
                    it.thumbUrl(),
                    R.drawable.placeholder_game,
                    onSongClicked(state.content().game)
                )
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        songs?.isEmpty() == true,
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_game_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterGameFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun onSongClicked(gameLoad: Async<Game>) =
        object : ImageNameCaptionListModel.EventHandler {
            override fun onClicked(clicked: ImageNameCaptionListModel) {
                viewModel.onSongClicked(
                    clicked.dataId,
                    clicked.name,
                    gameLoad.content()?.name ?: "",
                    hudState.selectedPart.apiId
                )
            }

            override fun clearClicked() {}
        }

    private fun Song.subtitleText() = when (composers?.size) {
        1 -> composers?.firstOrNull()?.name
            ?: resources.getString(R.string.subtitle_composer_unknown)
        else -> resources.getString(R.string.subtitle_composer_various)
    }

    private fun Song.thumbUrl() = Page.generateImageUrl(
        baseImageUrl,
        hudState.selectedPart,
        filename,
        1
    )

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}




