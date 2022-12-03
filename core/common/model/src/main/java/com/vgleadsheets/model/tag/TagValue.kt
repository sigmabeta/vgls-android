package com.vgleadsheets.model.tag

import com.vgleadsheets.model.Song

data class TagValue(
    val id: Long,
    val name: String,
    val tagKeyId: Long,
    val tagKeyName: String,
    val songs: List<Song>?
)
