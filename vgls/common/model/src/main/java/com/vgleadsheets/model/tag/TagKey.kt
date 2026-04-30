package com.vgleadsheets.model.tag

data class TagKey(
    val id: Long,
    val name: String,
    val values: List<TagValue>?
) {
    fun isDifficultyTag(): Boolean? = values?.all {
            it.isDifficultyValue()
        }
}
