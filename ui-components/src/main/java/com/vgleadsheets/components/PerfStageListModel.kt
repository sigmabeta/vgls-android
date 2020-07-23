package com.vgleadsheets.components

data class PerfStageListModel(
    val screenName: String,
    val perfStage: String,
    val duration: String
) : ListModel{
    override val dataId = (screenName + perfStage).hashCode().toLong()
    override val layoutId = R.layout.list_component_perf_stage
}
