package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.PerfViewMode
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

object PerfDisplay {
    fun getListModels(
        visible: Boolean,
        perfViewState: PerfViewState,
        loadTimeLists: Map<PerfSpec, ScreenLoadStatus>?,
        frameTimeStatsMap: Map<PerfSpec, FrameTimeStats>?,
        invalidateStatsMap: Map<PerfSpec, InvalidateStats>?,
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
            frameTimeStatsMap,
            invalidateStatsMap,
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
        frameTimeStatsMap: Map<PerfSpec, FrameTimeStats>?,
        invalidateStatsMap: Map<PerfSpec, InvalidateStats>?,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ): List<ListModel> {
        val loadTimes = loadTimeLists?.get(selectedScreen)
        val frameTimeStats = frameTimeStatsMap?.get(selectedScreen)
        val invalidateStats = invalidateStatsMap?.get(selectedScreen)

        return when (perfViewMode) {
            PerfViewMode.REGULAR -> perfSummaryForScreen(
                loadTimes,
                frameTimeStats,
                invalidateStats,
                onPerfCategoryClicked,
                resources
            )
            PerfViewMode.LOAD_TIMES -> loadTimesForScreen(
                loadTimes,
                resources
            )
            PerfViewMode.FRAME_TIMES -> frameTimesForScreen(
                frameTimeStats,
                resources
            )
            PerfViewMode.INVALIDATES -> invalidatesForScreen(
                invalidateStats,
                resources
            )
        }
    }

    private fun perfSummaryForScreen(
        perfScreenStatus: ScreenLoadStatus?,
        frameTimeStats: FrameTimeStats?,
        invalidateStats: InvalidateStats?,
        onPerfCategoryClicked: (PerfViewMode) -> Unit,
        resources: Resources
    ) = listOf(
        loadTimeSummary(perfScreenStatus, resources, onPerfCategoryClicked),
        frameTimeSummary(frameTimeStats, resources, onPerfCategoryClicked),
        invalidateSummary(invalidateStats, resources, onPerfCategoryClicked)
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
                perfScreenStatus.stageDurationMillis[PerfStage.COMPLETION]
            ),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.LOAD_TIMES)
        )
    } else {
        summaryEmptyLine(R.string.label_perf_empty_load_times, resources)
    }

    private fun frameTimeSummary(
        frameTimeStats: FrameTimeStats?,
        resources: Resources,
        onPerfCategoryClicked: (PerfViewMode) -> Unit
    ) = if (frameTimeStats != null) {
        LabelValueListModel(
            resources.getString(R.string.label_perf_summary_frame_drops),
            frameTimeStats.jankFrames.toString(),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.FRAME_TIMES)
        )
    } else {
        summaryEmptyLine(R.string.label_perf_empty_frame_times, resources)
    }

    private fun invalidateSummary(
        invalidateStats: InvalidateStats?,
        resources: Resources,
        onPerfCategoryClicked: (PerfViewMode) -> Unit
    ) = if (invalidateStats != null) {
        LabelValueListModel(
            resources.getString(R.string.label_perf_summary_invalidate),
            invalidateStats.jankInvalidates.toString(),
            categoryClickHandler(onPerfCategoryClicked, PerfViewMode.INVALIDATES)
        )
    } else {
        summaryEmptyLine(R.string.label_perf_empty_invalidates, resources)
    }

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
    ) = loadTimes?.stageDurationMillis?.map {
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
        frameTimeStats: FrameTimeStats?,
        resources: Resources
    ) = if (frameTimeStats != null) {
        listOf(
            LabelValueListModel(
                resources.getString(R.string.label_perf_frame_total),
                frameTimeStats.totalFrames.toString(),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_summary_frame_drops),
                frameTimeStats.jankFrames.toString(),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_frame_median),
                resources.getString(R.string.value_perf_ms, frameTimeStats.medianMillis),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_frame_five),
                resources.getString(R.string.value_perf_ms, frameTimeStats.ninetyFiveMillis),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_frame_nine),
                resources.getString(R.string.value_perf_ms, frameTimeStats.ninetyNineMillis),
                noopClicker()
            )
        )
    } else {
        listOf(
            summaryEmptyLine(R.string.label_perf_empty_frame_times, resources)
        )
    }

    private fun invalidatesForScreen(
        invalidateStats: InvalidateStats?,
        resources: Resources
    ) = if (invalidateStats != null) {
        listOf(
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_total),
                invalidateStats.totalInvalidates.toString(),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_jank),
                invalidateStats.jankInvalidates.toString(),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_total_time),
                resources.getString(
                    R.string.value_perf_ms,
                    invalidateStats.totalInvalidateTimeMillis
                ),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_median),
                resources.getString(R.string.value_perf_ms, invalidateStats.medianMillis),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_five),
                resources.getString(R.string.value_perf_ms, invalidateStats.medianMillis),
                noopClicker()
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_invalidate_nine),
                resources.getString(R.string.value_perf_ms, invalidateStats.medianMillis),
                noopClicker()
            )
        )
    } else {
        listOf(
            summaryEmptyLine(R.string.label_perf_empty_invalidates, resources)
        )
    }

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
