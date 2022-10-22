package com.vgleadsheets.model

data class SetlistEntry(
    val id: Long,
    val jamId: Long,
    val gameName: String,
    val songName: String,
    val song: Song?
)
