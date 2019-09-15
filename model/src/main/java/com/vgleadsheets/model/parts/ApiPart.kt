package com.vgleadsheets.model.parts

data class ApiPart(val part: String) {
    fun toPartEntity(songId: Long) = PartEntity(null, songId, part)
}
