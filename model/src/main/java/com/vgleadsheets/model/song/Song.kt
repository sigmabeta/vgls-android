package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.parts.Part

data class Song(
    val id: Long,
    val filename: String,
    val name: String,
    val composers: List<Composer>?,
    val parts: List<Part>?
)
