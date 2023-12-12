package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.Clicks
import com.vgleadsheets.features.main.hud.PartSelectorOption
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.HudMode
import com.vgleadsheets.nav.Modal
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.PerfViewState
import com.vgleadsheets.perf.tracking.common.FrameTimeStats
import com.vgleadsheets.perf.tracking.common.InvalidateStats
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.ScreenLoadStatus

object MenuRenderer {
    @Suppress("LongParameterList", "LongMethod")
    fun renderMenu(
        hudMode: HudMode,
        searchQuery: String?,
        searchResults: SearchContent,
        showVocalsOption: Boolean,
        selectedPart: Part,
        loadTimeLists: Map<PerfSpec, ScreenLoadStatus>?,
        frameTimeStatsMap: Map<PerfSpec, FrameTimeStats>?,
        invalidateStatsMap: Map<PerfSpec, InvalidateStats>?,
        refreshing: Boolean,
        updateTime: Async<Long>,
        currentSong: Song?,
        perfViewState: PerfViewState,
        baseImageUrl: String,
        navViewModel: NavViewModel,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {
        if (hudMode == HudMode.REGULAR && currentSong != null) {
            navViewModel.startHudVisibilityTimer()
        } else {
            navViewModel.stopHudTimer()
        }

        val menuItems = TitleBar.getListModels(
            PartSelectorOption.valueOf(selectedPart.name),
            hudMode,
            resources,
            clicks::searchButton,
            clicks::bottomMenuButton,
            clicks::changePart,
        ) + Search.getListModels(
            hudMode,
            searchQuery,
            selectedPart,
            searchResults,
            baseImageUrl,
            clicks,
            { text -> clicks.searchQuery(text) },
            clicks::bottomMenuButton,
            { clicks.searchClear() },
            resources
        ) + SongDisplay.getListModels(
            hudMode,
            currentSong,
            clicks::sheetDetail,
        ) + SongOptions.getListModels(
            hudMode,
            currentSong,
            clicks::sheetDetail,
            clicks::youtubeSearch,
            clicks::favorite,
            // clicks::offline,
            clicks::alternateSheet,
            resources
        ) + PartPicker.getListModels(
            hudMode == HudMode.PARTS,
            showVocalsOption,
            { clicks.part(it.name) },
            resources,
            selectedPart.apiId
        ) + MenuOptions.getListModels(
            hudMode == HudMode.MENU,
            refreshing,
            updateTime,
            { clicks.topLevelScreenLink(it) },
            { clicks.modalScreenLink(it) },
            { clicks.randomSelect(selectedPart) },
            { clicks.refresh() },
            { clicks.modalScreenLink(Modal.DEBUG_MENU) },
            { clicks.perf() },
            resources,
        ) + PerfDisplay.getListModels(
            hudMode == HudMode.PERF,
            perfViewState,
            loadTimeLists,
            frameTimeStatsMap,
            invalidateStatsMap,
            { clicks.perfScreenSelection(it) },
            { clicks.setPerfViewMode(it) },
            resources,
        )

        return menuItems
    }
}
