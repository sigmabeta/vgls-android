package com.vgleadsheets.features.main.songs

import android.content.res.Resources
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.ListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: SongListState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : ListConfig {
    override val titleConfig = Title.Config(
        resources.getString(com.vgleadsheets.ui_core.R.string.app_name),
        resources.getString(R.string.subtitle_all_songs),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { }
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !state.contentLoad.isNullOrEmpty()
    ) {
        val filteredGames = state.contentLoad.content()
            ?.filteredForVocals(hudState.selectedPart.apiId)

        if (filteredGames.isNullOrEmpty()) {
            return@Config listOf(
                EmptyStateListModel(
                    com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
                    resources.getString(com.vgleadsheets.features.main.list.R.string.empty_transposition),
                )
            )
        }

        val onlyTheHits = filteredGames
            .filter { it.isFavorite }
            .mapYielding {
                ImageNameCaptionListModel(
                    it.id + ListConfig.OFFSET_FAVORITE,
                    it.name,
                    it.gameName,
                    Page.generateThumbUrl(
                        baseImageUrl,
                        hudState.selectedPart.apiId,
                        it.isAltSelected,
                        it.filename
                    ),
                    com.vgleadsheets.vectors.R.drawable.ic_description_24dp
                ) { clicks.song(it.id) }
            }

        val filteredGameItems = filteredGames.mapYielding {
            ImageNameCaptionListModel(
                it.id,
                it.name,
                it.gameName,
                Page.generateThumbUrl(
                    baseImageUrl,
                    hudState.selectedPart.apiId,
                    it.isAltSelected,
                    it.filename
                ),
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp
            ) { clicks.song(it.id) }
        }

        val favoriteSection = if (onlyTheHits.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    resources.getString(com.vgleadsheets.features.main.list.R.string.section_header_favorites)
                )
            ) + onlyTheHits
        } else {
            emptyList()
        }

        val restOfThem = if (favoriteSection.isEmpty()) {
            emptyList()
        } else {
            listOf(
                SectionHeaderListModel(
                    resources.getString(R.string.section_header_all_songs)
                )
            )
        } + filteredGameItems

        return@Config favoriteSection + restOfThem
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_song)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        SongListFragment.LOAD_OPERATION,
        state.failure()?.message
            ?: resources.getString(com.vgleadsheets.features.main.list.R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )
}
