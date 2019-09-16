package com.vgleadsheets.model.parts

data class ApiPart(val part: String) {
    fun toPartEntity(id: Long, songId: Long) = PartEntity(id, songId, part)
}
