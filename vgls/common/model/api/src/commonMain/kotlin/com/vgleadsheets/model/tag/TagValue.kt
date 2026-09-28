package com.vgleadsheets.model.tag

import com.vgleadsheets.model.Song

data class TagValue(
    val id: Long,
    val name: String,
    val tagKeyId: Long,
    val tagKeyName: String,
    val songs: List<Song>?
) {
    fun isDifficultyValue(): Boolean {
        val valueAsNumber = name.toIntOrNull() ?: -1
        return valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM
    }

    companion object {
        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5
    }
}
