package com.vgleadsheets.features.main.game

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SubsectionHeaderListModel
import com.vgleadsheets.components.SubsectionListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.ListConfig
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
    private val state: GameDetailState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : ListConfig {
    private val gameLoad = state.game

    private val game = gameLoad.content()

    private val songsLoad = state.songs

    private val songs = songsLoad.content()

    private val composerLoad = state.composers

    private val composers = composerLoad.content()

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
        com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
        true,
        gameLoad.isLoading()
    )

    override val actionsConfig = Actions.Config(
        game != null,
        listOf(
            CtaListModel(
                if (game?.isFavorite == true) {
                    com.vgleadsheets.vectors.R.drawable.ic_jam_filled
                } else {
                    com.vgleadsheets.vectors.R.drawable.ic_jam_unfilled
                },
                resources.getString(
                    if (game?.isFavorite == true) {
                        com.vgleadsheets.features.main.hud.R.string.label_unfavorite
                    } else {
                        com.vgleadsheets.features.main.hud.R.string.label_favorite
                    }
                )
            ) { clicks.onFavoriteClick() }
        )
    )

    override val contentConfig = Content.Config(
        !songs.isNullOrEmpty()
    ) {

        val filteredSongs = songs?.filteredForVocals(hudState.selectedPart.apiId)
            ?.mapYielding { song ->
                ImageNameCaptionListModel(
                    song.id,
                    song.name,
                    song.subtitleText(),
                    song.thumbUrl(),
                    com.vgleadsheets.vectors.R.drawable.ic_album_24dp
                ) {
                    clicks.song(song.id)
                }
            }

        val composerModels = composers?.mapYielding { composer ->
            WideItemListModel(
                composer.id,
                composer.name,
                composer.photoUrl,
                com.vgleadsheets.vectors.R.drawable.ic_person_24dp,
                composer.id,
            ) {
                clicks.composer(composer.id)
            }
        }

        val contentModels = mutableListOf<ListModel>()

        if (!composerModels.isNullOrEmpty()) {
            if (composerModels.size == 1) {
                val composerModel = composerModels.first()
                val imageUrl = composerModel.imageUrl

                if (imageUrl != null) {
                    contentModels.add(
                        HeroImageListModel(
                            imageUrl,
                            composerModel.imagePlaceholder,
                            composerModel.name,
                            resources.getString(R.string.subtitle_composer_one),
                        ) {
                            clicks.composer(composerModel.dataId)
                        }
                    )
                } else {
                    contentModels.addComposersSubsection(composerModels)
                }
            } else {
                contentModels.addComposersSubsection(composerModels)
            }
        }

        contentModels.add(
            SectionHeaderListModel(resources.getString(R.string.section_header_songs_from_game))
        )

        if (!filteredSongs.isNullOrEmpty()) {
            contentModels.addAll(
                filteredSongs
            )
        } else {
            contentModels.add(
                EmptyStateListModel(
                    com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
                    resources.getString(com.vgleadsheets.features.main.list.R.string.empty_transposition),
                )
            )
        }

        return@Config contentModels
    }

    override val emptyConfig = EmptyState.Config(
        songs?.isEmpty() == true,
        com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_game_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        GameDetailFragment.LOAD_OPERATION,
        state.failure()?.message
            ?: resources.getString(com.vgleadsheets.features.main.list.R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun MutableList<ListModel>.addComposersSubsection(
        composerModels: List<WideItemListModel>
    ) {
        add(
            SubsectionListModel(
                id = R.string.section_header_composers.toLong(),
                titleModel = SubsectionHeaderListModel(
                    resources.getString(R.string.section_header_composers)
                ),
                children = composerModels
            )
        )
    }

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
        isAltSelected,
        1
    )

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}
