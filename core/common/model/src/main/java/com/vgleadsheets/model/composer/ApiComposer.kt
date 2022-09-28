package com.vgleadsheets.model.composer

@Suppress("ConstructorParameterNaming")
data class ApiComposer(
    val aliases: List<String>?,
    val composer_id: Long,
    val composer_name: String?,
    val image_url: String?
) {
    fun toComposerEntity(hasVocalSongs: Boolean) = com.vgleadsheets.database.model.ComposerEntity(
        composer_id + ID_OFFSET,
        composer_name ?: "Unknown Composer",
        hasVocalSongs,
        image_url
    )

    companion object {
        const val ID_OFFSET = 1000000000L
    }
}
