package com.vgleadsheets.appinfo

data class AppInfo(
    val isDebug: Boolean,
    val versionName: String,
    val versionCode: Int,
    val buildTimeMs: Long?,
    val buildBranch: String,
)
