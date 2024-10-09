package com.vgleadsheets.model.updates

data class AppUpdate(
    val versionCode: Int,
    val versionName: String,
    val releaseDate: Long,
    val changes: List<String>,
)
