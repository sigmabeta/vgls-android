package com.vgleadsheets.model.tag

data class TagKey(
    val id: Long,
    val name: String,
    val values: List<TagValue>
)
