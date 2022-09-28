package com.vgleadsheets.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.Song

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = "setlist_entry",
    foreignKeys = [
        ForeignKey(
            entity = JamEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("jam_id")
        )
    ]
)
data class SetlistEntryEntity(
    @PrimaryKey val id: Long,
    val game_name: String,
    val song_name: String,
    val jam_id: Long,
    val song_id: Long
) {
    fun toSetlistEntry(song: Song?) = SetlistEntry(
        id,
        game_name,
        song_name,
        song
    )
}
