package com.vgleadsheets.model.jam

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.Song

@Entity(tableName = "jam")
data class JamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val currentSheetId: Long
) {
    fun toJam(currentSong: Song, songHistory: List<SongHistoryEntry>?) = Jam(id!!, name, currentSong, songHistory)
}
