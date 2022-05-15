package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.PerfViewMode
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.PerfScreenStatus
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfState

object PerfDisplay {
    fun getListModels(
        visible: Boolean,
        perfViewState: PerfViewState,
        perfState: PerfState?,
        onScreenSelected: (PerfSpec) -> Unit,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ) = if (visible && perfState != null) {
        screenPicker(
            perfViewState.selectedScreen,
            onScreenSelected,
            resources
        ) + getPerfContentForScreen(
            perfViewState.selectedScreen,
            perfViewState.viewMode,
            perfState,
            onPerfCategoryClicked,
            resources
        )
    } else {
        emptyList()
    }

    private fun getPerfContentForScreen(
        selectedScreen: PerfSpec,
        perfViewMode: PerfViewMode,
        perfState: PerfState,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ): List<ListModel> {
        val perfScreenStatus = perfState.screens[selectedScreen]

        if (perfScreenStatus == null) {
            return listOf(
                LabelValueListModel(
                    "No perf data recorded.",
                    "",
                    noopClicker()
                )
            )
        }

        return when (perfViewMode) {
            PerfViewMode.REGULAR -> perfSummaryForScreen(
                perfScreenStatus,
                onPerfCategoryClicked,
                resources
            )
            PerfViewMode.LOAD_TIMES -> loadTimesForScreen(
                perfScreenStatus,
                resources
            )
            PerfViewMode.FRAME_TIMES -> frameTimesForScreen(
                perfScreenStatus,
                perfState
            )
            PerfViewMode.INVALIDATES -> invalidatesForScreen(
                selectedScreen,
                perfState
            )
        }
    }

    private fun perfSummaryForScreen(
        perfScreenStatus: PerfScreenStatus,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ) = listOf(
        LabelValueListModel(
            resources.getString(R.string.label_perf_summary_completion),
            resources.getString(
                R.string.value_perf_ms,
                perfScreenStatus.stageDurations[PerfStage.COMPLETION]
            ),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.LOAD_TIMES)
        )
    )

    private fun screenPicker(
        selectedScreen: PerfSpec,
        onScreenSelected: (PerfSpec) -> Unit,
        resources: Resources
    ): List<ListModel> {
        val perfSpecs = PerfSpec.values().toList()
        return listOf(
            DropdownSettingListModel(
                "PerfSpec",
                resources.getString(R.string.label_perf_stats_for),
                perfSpecs.indexOf(selectedScreen),
                perfSpecs.map { it.name },
                dropdownHandler { onScreenSelected(it) }
            )
        )
    }

    private fun loadTimesForScreen(
        perfScreenStatus: PerfScreenStatus,
        resources: Resources
    ) = perfScreenStatus.stageDurations.map {
        LabelValueListModel(
            it.key.name,
            resources.getString(R.string.value_perf_ms, it.value),
            noopClicker()
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun frameTimesForScreen(
        perfScreenStatus: PerfScreenStatus,
        perfState: PerfState
    ) = todo()

    @Suppress("UNUSED_PARAMETER")
    private fun invalidatesForScreen(
        selectedScreen: PerfSpec,
        perfState: PerfState
    ) = todo()

    private fun todo() = listOf(
        LabelValueListModel(
            "Not implemented yet",
            "LOL",
            noopClicker()
        )
    )

    private fun dropdownHandler(onOptionSelected: (PerfSpec) -> Unit) =
        object : DropdownSettingListModel.EventHandler {
            override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
                onOptionSelected(PerfSpec.values()[selectedPosition])
            }
        }

    private fun categoryClickHandler(
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        perfViewMode: PerfViewMode
    ) = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) {
            onPerfCategoryClicked(perfViewMode)
        }

        override fun clearClicked() = Unit
    }

    private fun noopClicker() = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) = Unit

        override fun clearClicked() = Unit
    }
}
