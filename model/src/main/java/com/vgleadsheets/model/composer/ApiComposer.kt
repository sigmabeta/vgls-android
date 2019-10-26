package com.vgleadsheets.model.composer

data class ApiComposer(
    val id: Long,
    val name: String
) {
    fun toComposerEntity() = ComposerEntity(id + ID_OFFSET, name)

    companion object {
        const val ID_OFFSET = 1000000000L
    }
}
