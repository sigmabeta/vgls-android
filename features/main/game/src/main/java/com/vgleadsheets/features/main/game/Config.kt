package com.vgleadsheets.features.main.game

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
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
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: GameState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig {
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

    override val actionsConfig = Actions.Config(
        game != null,
        listOf(
            CtaListModel(
                if (game?.isFavorite == true) {
                    R.drawable.ic_jam_filled
                } else {
                    R.drawable.ic_jam_unfilled
                },
                resources.getString(
                    if (game?.isFavorite == true) {
                        R.string.label_unfavorite
                    } else {
                        R.string.label_favorite
                    }
                )
            ) { clicks.onFavoriteClick() }
        )
    )

    override val contentConfig = Content.Config(
        !songs.isNullOrEmpty()
    ) {
        songs?.filteredForVocals(hudState.selectedPart.apiId)
            ?.mapYielding { song ->
                ImageNameCaptionListModel(
                    song.id,
                    song.name,
                    song.subtitleText(),
                    song.thumbUrl(),
                    R.drawable.placeholder_game
                ) {
                    clicks.song(song.id)
                }
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
        GameFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun Song.subtitleText() = when (composers?.size) {
        null -> resources.getString(R.string.subtitle_composer_unknown)
        0 -> resources.getString(R.string.subtitle_composer_unknown)
        1 -> composers!!.first().name
        else -> resources.getString(R.string.subtitle_composer_various)
    }

    private fun Song.thumbUrl() = Page.generateImageUrl(
        baseImageUrl,
        hudState.selectedPart.apiId,
        filename,
        1
    )

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}
