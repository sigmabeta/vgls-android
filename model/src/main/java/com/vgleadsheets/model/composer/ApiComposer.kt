package com.vgleadsheets.model.composer

data class ApiComposer(
    val id: Long,
    val name: String
) {
    fun toComposerEntity() = ComposerEntity(id, name)
}