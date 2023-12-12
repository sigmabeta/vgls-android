package com.vgleadsheets.features.main.hud

import com.vgleadsheets.model.Part
import com.vgleadsheets.nav.HudMode
import com.vgleadsheets.nav.Modal
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.Navigator
import com.vgleadsheets.nav.PerfViewMode
import com.vgleadsheets.nav.TopLevel
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

class Clicks(
    private val hudViewModel: HudViewModel,
    private val navViewModel: NavViewModel,
    private val navigator: Navigator,
    private val tracker: Tracker,
    private val trackingDetails: String
) {
    fun shadow() {
        tracker.logShadowClick()
    }

    fun searchButton() {
        tracker.logSearchButtonClick()
        navViewModel.showSearch()
    }

    fun searchQuery(query: String) {
        val trimmedQuery = query.trim()

        if (trimmedQuery.length > 2) {
            tracker.logSearch(trimmedQuery)
            hudViewModel.queueSearchQuery(trimmedQuery)
        }
    }

    fun back(navState: NavState) = when (navState.hudMode) {
        HudMode.PERF -> {
            when (navState.perfViewState.viewMode) {
                PerfViewMode.REGULAR -> navViewModel.toMenu()
                else -> navViewModel.toPerf()
            }
            true
        }

        HudMode.SEARCH -> {
            navViewModel.toRegularMode()
            true
        }

        HudMode.REGULAR -> {
            false
        }

        else -> {
            navViewModel.toRegularMode()
            true
        }
    }

    fun part(name: String) {
        tracker.logPartSelect(name)
        navViewModel.onPartSelect(name)
    }

    fun appBarButton(state: NavState) {
        tracker.logAppBarButtonClick()
        when (state.hudMode) {
            HudMode.SEARCH -> navViewModel.toRegularMode()
            HudMode.REGULAR -> {
                if (state.alwaysShowBack) {
                    navigator.back()
                } else {
                    navViewModel.toMenu()
                }
            }

            else -> {
                if (state.alwaysShowBack) {
                    navigator.back()
                } else {
                    navViewModel.toRegularMode()
                }
            }
        }
    }

    fun bottomMenuButton() {
        tracker.logBottomMenuButtonClick()
        navViewModel.bottomMenuButtonClick()
    }

    fun sheetDetail() {
        // tracker.logSheetDetailClick()
        navViewModel.sheetDetailClick()
    }

    fun youtubeSearch() {
        // tracker.logSheetDetailClick()
        navViewModel.youtubeSearchClick()
    }

    fun favorite() {
        navViewModel.favoritesClick()
    }

    fun alternateSheet() {
        navViewModel.alternateSheetClick()
    }

    fun offline() {
        navViewModel.offlineClick()
    }

    fun changePart() {
        tracker.logChangePartClick()
        navViewModel.onChangePartClick()
    }

    fun topLevelScreenLink(screen: TopLevel) {
        val fromScreen = TrackingScreen.HUD

        tracker.logScreenLinkClick(screen.name, fromScreen, trackingDetails)

        when (screen) {
            TopLevel.FAVORITE -> navigator.showFavorites(fromScreen, trackingDetails)
            TopLevel.GAME -> navigator.showGameList(fromScreen, trackingDetails)
            TopLevel.COMPOSER -> navigator.showComposerList(fromScreen, trackingDetails)
            TopLevel.TAG -> navigator.showTagList(fromScreen, trackingDetails)
            TopLevel.SONG -> navigator.showAllSheets(fromScreen, trackingDetails)
            else -> navigator.showGameList(fromScreen, trackingDetails)
        }
    }

    fun modalScreenLink(screen: Modal) {
        val fromScreen = TrackingScreen.HUD

        tracker.logScreenLinkClick(screen.name, fromScreen, trackingDetails)

        when (screen) {
            Modal.SETTINGS -> navigator.showSettings(fromScreen, trackingDetails)
            Modal.DEBUG_MENU -> navigator.showDebug(fromScreen, trackingDetails)
            else -> navigator.showGameList(fromScreen, trackingDetails)
        }
    }

    fun randomSelect(selectedPart: Part) {
        tracker.logRandomClick()
        navViewModel.onRandomSelectClick(selectedPart)
    }

    fun refresh() {
        tracker.logRefreshClick()
        navViewModel.refresh()
    }

    fun perf() {
        navViewModel.toPerf()
    }

    fun perfScreenSelection(screen: PerfSpec) {
        navViewModel.setPerfSelectedScreen(screen)
    }

    fun setPerfViewMode(mode: PerfViewMode) {
        navViewModel.setPerfViewMode(mode)
    }

    fun searchClear() {
        hudViewModel
            .clearSearchQuery()
    }

    fun songSearchResult(id: Long) {
        navigator.showSongViewer(id)
    }

    fun gameSearchResult(id: Long) {
        navigator.showGameDetail(id)
    }

    fun composerSearchResult(id: Long) {
        navigator.showComposerDetail(id)
    }

    fun showMoreResults(query: String) {
        navigator.showSearch(query)
    }
}
