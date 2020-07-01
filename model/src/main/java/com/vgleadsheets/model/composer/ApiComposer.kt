package com.vgleadsheets.model.composer

data class ApiComposer(
    val aliases: List<String>?,
    val composer_id: Long,
    val composer_name: String,
    val image_url: String?
) {
    fun toComposerEntity() = ComposerEntity(composer_id + ID_OFFSET, composer_name, image_url)

    companion object {
        const val ID_OFFSET = 1000000000L
    }
}
