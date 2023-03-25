package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.Clicks
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.PartSelectorOption
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
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
        activeJam: Jam?,
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
        viewModel: HudViewModel,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {
        if (hudMode == HudMode.REGULAR && currentSong != null) {
            viewModel.startHudVisibilityTimer()
        } else {
            viewModel.stopHudTimer()
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
        ) + JamDisplay.getListModels(
            hudMode,
            activeJam,
            clicks::jamDetail,
            clicks::jamSong,
            clicks::jamUnfollow,
            resources,
        ) + SongDisplay.getListModels(
            hudMode,
            currentSong,
            activeJam,
            clicks::sheetDetail,
        ) + SongOptions.getListModels(
            hudMode,
            currentSong,
            clicks::sheetDetail,
            clicks::youtubeSearch,
            clicks::favorite,
            // clicks::offline,
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
            { clicks.screenLink(it) },
            { clicks.randomSelect(selectedPart) },
            { clicks.refresh() },
            { clicks.screenLink(HudFragment.MODAL_SCREEN_ID_DEBUG) },
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
        ) + RefreshIndicator.getListModels(
            refreshing,
            resources,
        )

        return menuItems
    }
}
