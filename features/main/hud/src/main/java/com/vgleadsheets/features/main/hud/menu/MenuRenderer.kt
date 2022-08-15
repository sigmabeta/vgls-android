package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.Clicks
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

object MenuRenderer {
    @Suppress("LongParameterList", "LongMethod")
    fun renderMenu(
        hudMode: HudMode,
        searchQuery: String?,
        searchResults: SearchContent,
        shouldHide: Boolean,
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
        router: FragmentRouter,
        viewModel: HudViewModel,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {

        val songDetailClickHandler = {
            if (currentSong != null) {
                router.showSheetDetail(currentSong.id)
            }
        }

        val youtubeSearchClickHandler = {
            if (currentSong != null) {
                router.searchYoutube(currentSong.name, currentSong.gameName)
            }
        }

        if (hudMode == HudMode.REGULAR && shouldHide) {
            viewModel.startHudTimer()
        } else {
            viewModel.stopHudTimer()
        }

        val menuItems = TitleBar.getListModels(
            PartSelectorOption.valueOf(selectedPart.name),
            hudMode,
            resources,
            { clicks.searchButton() },
            { clicks.bottomMenuButton(hudMode, perfViewState.viewMode) },
            { clicks.changePart() },
        ) + Search.getListModels(
            hudMode,
            searchQuery,
            selectedPart,
            searchResults,
            baseImageUrl,
            clicks,
            { text -> clicks.searchQuery(text) },
            { clicks.bottomMenuButton(hudMode, perfViewState.viewMode) },
            { clicks.searchClear() },
            resources
        ) + SongDisplay.getListModels(
            hudMode,
            currentSong,
            songDetailClickHandler,
        ) + SheetOptions.getListModels(
            hudMode == HudMode.REGULAR,
            currentSong,
            songDetailClickHandler,
            youtubeSearchClickHandler,
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
