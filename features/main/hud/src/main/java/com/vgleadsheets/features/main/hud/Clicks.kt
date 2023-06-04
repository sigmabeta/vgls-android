package com.vgleadsheets.features.main.hud

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.hud.HudFragment.Companion.MODAL_SCREEN_ID_DEBUG
import com.vgleadsheets.features.main.hud.HudFragment.Companion.MODAL_SCREEN_ID_SETTINGS
import com.vgleadsheets.features.main.hud.HudFragment.Companion.TOP_LEVEL_SCREEN_ID_COMPOSER
import com.vgleadsheets.features.main.hud.HudFragment.Companion.TOP_LEVEL_SCREEN_ID_FAVORITES
import com.vgleadsheets.features.main.hud.HudFragment.Companion.TOP_LEVEL_SCREEN_ID_GAME
import com.vgleadsheets.features.main.hud.HudFragment.Companion.TOP_LEVEL_SCREEN_ID_SONG
import com.vgleadsheets.features.main.hud.HudFragment.Companion.TOP_LEVEL_SCREEN_ID_TAG
import com.vgleadsheets.model.Part
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

class Clicks(
    private val viewModel: HudViewModel,
    private val router: FragmentRouter,
    private val tracker: Tracker,
    private val trackingDetails: String
) {
    fun shadow() {
        tracker.logShadowClick()
        viewModel.toRegularMode()
    }

    fun searchButton() {
        tracker.logSearchButtonClick()
        viewModel.showSearch()
    }

    fun searchQuery(query: String) {
        val trimmedQuery = query.trim()

        if (trimmedQuery.length > 2) {
            tracker.logSearch(trimmedQuery)
            viewModel.queueSearchQuery(trimmedQuery)
        }
    }

    fun back(hudState: HudState) = when (hudState.mode) {
        HudMode.PERF -> {
            when (hudState.perfViewState.viewMode) {
                PerfViewMode.REGULAR -> viewModel.toMenu()
                else -> viewModel.toPerf()
            }
            true
        }

        HudMode.SEARCH -> {
            viewModel.toRegularMode()
            true
        }

        HudMode.REGULAR -> {
            false
        }

        else -> {
            viewModel.toRegularMode()
            true
        }
    }

    fun part(name: String) {
        tracker.logPartSelect(name)
        viewModel.onPartSelect(name)
    }

    fun appBarButton(state: HudState) {
        tracker.logAppBarButtonClick()
        when (state.mode) {
            HudMode.SEARCH -> viewModel.toRegularMode()
            HudMode.REGULAR -> {
                if (state.alwaysShowBack) {
                    router.back()
                } else {
                    viewModel.toMenu()
                }
            }

            else -> {
                if (state.alwaysShowBack) {
                    router.back()
                } else {
                    viewModel.toRegularMode()
                }
            }
        }
    }

    fun bottomMenuButton() {
        tracker.logBottomMenuButtonClick()
        viewModel.bottomMenuButtonClick()
    }

    fun sheetDetail() {
        // tracker.logSheetDetailClick()
        viewModel.sheetDetailClick()
    }

    fun youtubeSearch() {
        // tracker.logSheetDetailClick()
        viewModel.youtubeSearchClick()
    }

    fun favorite() {
        viewModel.favoritesClick()
    }

    fun alternateSheet() {
        viewModel.alternateSheetClick()
    }

    fun offline() {
        viewModel.offlineClick()
    }

    fun changePart() {
        tracker.logChangePartClick()
        viewModel.onChangePartClick()
    }

    fun screenLink(screenId: String) {
        val fromScreen = TrackingScreen.HUD

        tracker.logScreenLinkClick(screenId, fromScreen, trackingDetails)

        viewModel.toRegularMode()
        viewModel.saveTopLevelScreen(screenId)

        when (screenId) {
            TOP_LEVEL_SCREEN_ID_FAVORITES -> router.showFavorites(fromScreen, trackingDetails)
            TOP_LEVEL_SCREEN_ID_GAME -> router.showGameList(fromScreen, trackingDetails)
            TOP_LEVEL_SCREEN_ID_COMPOSER -> router.showComposerList(fromScreen, trackingDetails)
            TOP_LEVEL_SCREEN_ID_TAG -> router.showTagList(fromScreen, trackingDetails)
            TOP_LEVEL_SCREEN_ID_SONG -> router.showAllSheets(fromScreen, trackingDetails)
            MODAL_SCREEN_ID_SETTINGS -> router.showSettings(fromScreen, trackingDetails)
            MODAL_SCREEN_ID_DEBUG -> router.showDebug(fromScreen, trackingDetails)
            else -> router.showGameList(fromScreen, trackingDetails)
        }
    }

    fun randomSelect(selectedPart: Part) {
        tracker.logRandomClick()
        viewModel.toRegularMode()
        viewModel.onRandomSelectClick(selectedPart)
    }

    fun refresh() {
        tracker.logRefreshClick()
        viewModel.refresh()
        viewModel.toRegularMode()
    }

    fun perf() {
        viewModel.toPerf()
    }

    fun perfScreenSelection(screen: PerfSpec) {
        viewModel.setPerfSelectedScreen(screen)
    }

    fun setPerfViewMode(mode: PerfViewMode) {
        viewModel.setPerfViewMode(mode)
    }

    fun searchClear() {
        viewModel.clearSearchQuery()
    }

    fun songSearchResult(id: Long) {
        router.showSongViewer(id)
        viewModel.toRegularMode()
    }

    fun gameSearchResult(id: Long) {
        router.showGameDetail(id)
        viewModel.toRegularMode()
    }

    fun composerSearchResult(id: Long) {
        router.showSongListForComposer(id)
        viewModel.toRegularMode()
    }

    fun showMoreResults(query: String) {
        router.showSearch(query)
        viewModel.toRegularMode()
    }
}
