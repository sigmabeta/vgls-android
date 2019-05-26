package com.vgleadsheets.model.song

data class ApiSong(
    val id: Long,
    val filename: String,
    val name: String,
    val pageCount: Int) {
    fun toSong() = Song(id, filename, name, pageCount)
}