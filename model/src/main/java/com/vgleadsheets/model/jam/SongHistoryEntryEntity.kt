package com.vgleadsheets.model.jam

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.Song

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = "song_history_entry",
    foreignKeys = [
        ForeignKey(
            entity = JamEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("jam_id")
        )
    ]
)
data class SongHistoryEntryEntity(
    @PrimaryKey val id: Long,
    val jam_id: Long,
    val song_id: Long
) {
    fun toSongHistoryEntry(song: Song?) = SongHistoryEntry(
        id,
        song
    )
}
