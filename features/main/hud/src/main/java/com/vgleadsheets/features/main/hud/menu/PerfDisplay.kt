package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.PerfViewMode
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.FrameInfo
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

object PerfDisplay {
    fun getListModels(
        visible: Boolean,
        perfViewState: PerfViewState,
        loadTimeLists: Map<PerfSpec, ScreenLoadStatus>?,
        frameTimeLists: Map<PerfSpec, List<FrameInfo>>?,
        onScreenSelected: (PerfSpec) -> Unit,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ) = if (visible) {
        screenPicker(
            perfViewState.selectedScreen,
            onScreenSelected,
            resources
        ) + getPerfContentForScreen(
            perfViewState.selectedScreen,
            perfViewState.viewMode,
            loadTimeLists,
            frameTimeLists,
            onPerfCategoryClicked,
            resources
        )
    } else {
        emptyList()
    }

    private fun getPerfContentForScreen(
        selectedScreen: PerfSpec,
        perfViewMode: PerfViewMode,
        loadTimeLists: Map<PerfSpec, ScreenLoadStatus>?,
        frameTimeLists: Map<PerfSpec, List<FrameInfo>>?,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ): List<ListModel> {
        val loadTimes = loadTimeLists?.get(selectedScreen)
        val frameTimes = frameTimeLists?.get(selectedScreen)

        return when (perfViewMode) {
            PerfViewMode.REGULAR -> perfSummaryForScreen(
                loadTimes,
                frameTimes,
                onPerfCategoryClicked,
                resources
            )
            PerfViewMode.LOAD_TIMES -> loadTimesForScreen(
                loadTimes,
                resources
            )
            PerfViewMode.FRAME_TIMES -> frameTimesForScreen(
                frameTimes,
                resources
            )
            PerfViewMode.INVALIDATES -> invalidatesForScreen(
                // invalidates
            )
        }
    }

    private fun perfSummaryForScreen(
        perfScreenStatus: ScreenLoadStatus?,
        frameTimes: List<FrameInfo>?,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ) = listOf(
        loadTimeSummary(perfScreenStatus, resources, onPerfCategoryClicked),
        frameTimeSummary(frameTimes, resources, onPerfCategoryClicked),
        invalidateSummary(resources/*, onPerfCategoryClicked*/)
    )

    private fun loadTimeSummary(
        perfScreenStatus: ScreenLoadStatus?,
        resources: Resources,
        onPerfCategoryClicked: (PerfViewMode) -> Unit
    ) = if (perfScreenStatus != null) {
        LabelValueListModel(
            resources.getString(R.string.label_perf_summary_completion),
            resources.getString(
                R.string.value_perf_ms,
                perfScreenStatus.stageDurations[PerfStage.COMPLETION]
            ),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.LOAD_TIMES)
        )
    } else {
        summaryEmptyLine(R.string.label_perf_empty_load_times, resources)
    }

    private fun frameTimeSummary(
        frameTimes: List<FrameInfo>?,
        resources: Resources,
        onPerfCategoryClicked: (PerfViewMode) -> Unit
    ) = if (frameTimes != null) {
        LabelValueListModel(
            resources.getString(R.string.label_perf_summary_frame_drops),
            frameTimes.filter { it.isJank }.size.toString(),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.FRAME_TIMES)
        )
    } else {
        summaryEmptyLine(R.string.label_perf_empty_frame_times, resources)
    }

    private fun invalidateSummary(
        // invalidates: List<InvalidateInfo>?,
        resources: Resources,
        // onPerfCategoryClicked: (PerfViewMode) -> Unit
    ) = // if (invalidates != null) {
    // LabelValueListModel(
    //     resources.getString(R.string.label_perf_summary_frame_drops),
    //     something lol,
    //     categoryClickHandler(onPerfCategoryClicked, PerfViewMode.FRAME_TIMES)
    // )
        // } else {
        summaryEmptyLine(R.string.label_perf_empty_invalidates, resources)
    // }

    private fun summaryEmptyLine(
        labelId: Int,
        resources: Resources
    ) = LabelValueListModel(
        resources.getString(labelId),
        "",
        noopClicker()
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
        loadTimes: ScreenLoadStatus?,
        resources: Resources
    ) = loadTimes?.stageDurations?.map {
        LabelValueListModel(
            it.key.name,
            resources.getString(R.string.value_perf_ms, it.value),
            noopClicker()
        )
    } ?: listOf(
        summaryEmptyLine(R.string.label_perf_empty_load_times, resources)
    )

    @Suppress("UNUSED_PARAMETER")
    private fun frameTimesForScreen(
        frameTimes: List<FrameInfo>?,
        resources: Resources
    ) = if (frameTimes != null) {
        listOf(
            LabelValueListModel(
                resources.getString(R.string.label_perf_frame_total),
                frameTimes.size.toString(),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_summary_frame_drops),
                frameTimes.filter { it.isJank }.size.toString(),
                noopClicker()
            )
        )
    } else {
        listOf(
            summaryEmptyLine(R.string.label_perf_empty_frame_times, resources)
        )
    }

    private fun invalidatesForScreen() = todo()

    private fun todo() = listOf(
        LabelValueListModel(
            "Not implemented yet",
            "LOL",
            noopClicker()
        ),
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
