package com.vgleadsheets.components

data class PerfStageListModel(
    val screenName: String,
    val startTime: Long,
    val perfStage: String,
    val duration: String,
    val targetTime: Long
) : ListModel{
    override val dataId = (screenName + perfStage).hashCode().toLong()
    override val layoutId = R.layout.list_component_perf_stage
}
