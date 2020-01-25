package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.parts.Part

data class Song(
    val id: Long,
    val name: String,
    val gameName: String,
    val composers: List<Composer>?,
    val parts: List<Part>?
)
