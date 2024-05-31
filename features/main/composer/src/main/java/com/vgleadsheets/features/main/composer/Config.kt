package com.vgleadsheets.features.main.composer

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
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
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: ComposerDetailState,
    private val navState: NavState,
    private val basesourceInfo: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : ListConfig {
    private val composerLoad = state.composer

    private val composer = composerLoad.content()

    private val songsLoad = state.songs

    private val songs = songsLoad.content()

    private val gameLoad = state.games

    private val games = gameLoad.content()

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
        com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
        true,
        composerLoad.isLoading(),
    )

    override val actionsConfig = Actions.Config(
        composer != null,
        listOf(
            CtaListModel(
                if (composer?.isFavorite == true) {
                    com.vgleadsheets.ui.icons.R.drawable.ic_jam_filled
                } else {
                    com.vgleadsheets.ui.icons.R.drawable.ic_jam_unfilled
                },
                resources.getString(
                    if (composer?.isFavorite == true) {
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
        val songModels = songs
            ?.mapYielding { song ->
                ImageNameCaptionListModel(
                    song.id,
                    song.name,
                    song.captionText(),
                    Page.generateThumbUrl(
                        basesourceInfo,
                        navState.selectedPart.apiId,
                        song.isAltSelected,
                        song.filename
                    ),
                    com.vgleadsheets.ui.icons.R.drawable.ic_description_24dp
                ) {
                    clicks.song(song.id)
                }
            }

        val gameModels = games?.mapYielding { game ->
            SquareItemListModel(
                game.id,
                game.name,
                game.photoUrl,
                com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
                game.id,
            ) {
                clicks.game(game.id)
            }
        }

        val contentModels = mutableListOf<ListModel>()

        if (!gameModels.isNullOrEmpty()) {
            if (gameModels.size == 1) {
                val gameModel = gameModels.first()
                val sourceInfo = gameModel.sourceInfo

                if (sourceInfo != null) {
                    contentModels.add(
                        SectionHeaderListModel(
                            resources.getString(R.string.section_header_games)
                        )
                    )

                    contentModels.add(
                        HeroImageListModel(
                            sourceInfo,
                            gameModel.imagePlaceholder,
                            gameModel.name,
                        ) {
                            clicks.game(gameModel.dataId)
                        }
                    )
                } else {
                    contentModels.addGamesSubsection(gameModels)
                }
            } else {
                contentModels.addGamesSubsection(gameModels)
            }
        }

        contentModels.add(
            SectionHeaderListModel(resources.getString(R.string.section_header_songs_by_composer))
        )

        if (!songModels.isNullOrEmpty()) {
            contentModels.addAll(
                songModels
            )
        } else {
            contentModels.add(
                EmptyStateListModel(
                    com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
                    resources.getString(com.vgleadsheets.features.main.list.R.string.empty_transposition),
                )
            )
        }

        return@Config contentModels
    }

    override val emptyConfig = EmptyState.Config(
        songs?.isEmpty() == true,
        com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_composer_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        ComposerDetailFragment.LOAD_OPERATION,
        state.failure()?.message
            ?: resources.getString(com.vgleadsheets.features.main.list.R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun MutableList<ListModel>.addGamesSubsection(
        gameModels: List<SquareItemListModel>
    ) {
        add(
            SectionHeaderListModel(
                resources.getString(R.string.section_header_games)
            )
        )

        addAll(gameModels)
    }

    private fun Song.captionText() = gameName

    private fun List<Song>.captionText() = resources.getString(
        R.string.subtitle_sheets_count,
        size
    )
}
