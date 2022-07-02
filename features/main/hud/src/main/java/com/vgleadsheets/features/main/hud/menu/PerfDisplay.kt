package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.PerfViewMode
import com.vgleadsheets.features.main.hud.PerfViewState
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

@Suppress("LongParameterList", "TooManyFunctions")
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
        SectionHeaderListModel(resources.getString(R.string.label_perf_summary)),
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
            resources.getString(R.string.label_perf_load_times),
            resources.getString(
                R.string.value_perf_summary_completion,
                perfScreenStatus.stageDurationMillis[PerfStage.COMPLETION]
            ),
            { onPerfCategoryClicked(PerfViewMode.LOAD_TIMES) }
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
            resources.getString(R.string.label_perf_frame_times),
            resources.getString(
                R.string.value_perf_summary_frame_drops,
                frameTimeStats.jankFrames,
                frameTimeStats.totalFrames
            ),
            { onPerfCategoryClicked(PerfViewMode.FRAME_TIMES) }
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
            resources.getString(R.string.label_perf_invalidates),
            resources.getString(
                R.string.value_perf_summary_invalidate,
                invalidateStats.jankInvalidates,
                invalidateStats.totalInvalidates
            ),
            { onPerfCategoryClicked(PerfViewMode.INVALIDATES) }
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
        onClick = NO_ACTION
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
                perfSpecs.map { it.name }
            ) { position -> onScreenSelected(perfSpecs[position]) }
        )
    }

    private fun loadTimesForScreen(
        loadTimes: ScreenLoadStatus?,
        resources: Resources
    ) = loadTimeHeader(resources) + loadTimeContent(loadTimes, resources)

    private fun loadTimeHeader(resources: Resources) = listOf(
        SectionHeaderListModel(
            resources.getString(R.string.label_perf_invalidates)
        )
    )

    private fun loadTimeContent(
        loadTimes: ScreenLoadStatus?,
        resources: Resources
    ) = loadTimes
        ?.stageDurationMillis
        ?.map {
            LabelValueListModel(
                resources.getString(it.key.getOnScreenNameId()),
                resources.getString(R.string.value_perf_ms, it.value),
                onClick = NO_ACTION
            )
        } ?: listOf(summaryEmptyLine(R.string.label_perf_empty_load_times, resources))

    @Suppress("UNUSED_PARAMETER")
    private fun frameTimesForScreen(
        frameTimeStats: FrameTimeStats?,
        resources: Resources
    ) = if (frameTimeStats != null) {
        listOf(
            SectionHeaderListModel(
                resources.getString(R.string.label_perf_frame_times)
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_total),
                frameTimeStats.totalFrames.toString(),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_jank),
                frameTimeStats.jankFrames.toString(),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_median),
                resources.getString(R.string.value_perf_ms, frameTimeStats.medianMillis),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_five),
                resources.getString(R.string.value_perf_ms, frameTimeStats.ninetyFiveMillis),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_nine),
                resources.getString(R.string.value_perf_ms, frameTimeStats.ninetyNineMillis),
                onClick = NO_ACTION
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
            SectionHeaderListModel(
                resources.getString(R.string.label_perf_invalidates)
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_total),
                invalidateStats.totalInvalidates.toString(),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_jank),
                invalidateStats.jankInvalidates.toString(),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_total_time),
                resources.getString(
                    R.string.value_perf_ms,
                    invalidateStats.totalInvalidateTimeMillis
                ),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_median),
                resources.getString(R.string.value_perf_ms, invalidateStats.medianMillis),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_five),
                resources.getString(R.string.value_perf_ms, invalidateStats.ninetyFiveMillis),
                onClick = NO_ACTION
            ),
            LabelValueListModel(
                resources.getString(R.string.label_perf_nine),
                resources.getString(R.string.value_perf_ms, invalidateStats.ninetyNineMillis),
                onClick = NO_ACTION
            )
        )
    } else {
        listOf(
            summaryEmptyLine(R.string.label_perf_empty_invalidates, resources)
        )
    }

    private fun PerfStage.getOnScreenNameId() = when (this) {
        PerfStage.VIEW_CREATED -> R.string.label_perf_stage_view_created
        PerfStage.TITLE_LOADED -> R.string.label_perf_stage_title_loaded
        PerfStage.TRANSITION_START -> R.string.label_perf_stage_transition_start
        PerfStage.PARTIAL_CONTENT_LOAD -> R.string.label_perf_stage_partial_content
        PerfStage.FULL_CONTENT_LOAD -> R.string.label_perf_stage_full_content
        PerfStage.CANCELLATION -> R.string.label_perf_stage_cancelled
        PerfStage.COMPLETION -> R.string.label_perf_stage_completed
    }

    val NO_ACTION = { }
}
