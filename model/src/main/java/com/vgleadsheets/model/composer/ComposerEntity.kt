package com.vgleadsheets.model.composer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.Song

@Entity(
    tableName = "composer"
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    var hasVocalSongs: Boolean = false,
    val photoUrl: String? = null
) {
    fun toComposer(songs: List<Song>?) = Composer(id, name, songs, photoUrl)
}
