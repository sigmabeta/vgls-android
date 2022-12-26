package com.vgleadsheets.model.alias

import com.vgleadsheets.model.Song

data class SongAlias(
    val id: Long?,
    val songId: Long,
    val name: String,
    val song: Song?
)
