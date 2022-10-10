package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry
import com.vgleadsheets.network.model.ApiSongHistoryEntry

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
)

fun SongHistoryEntryEntity.toSongHistoryEntry(song: Song?) = SongHistoryEntry(
    id,
    song
)

fun ApiSongHistoryEntry.toSongHistoryEntryEntity(jamId: Long, listPosition: Int) = SongHistoryEntryEntity(
    MULTIPLIER_JAM_ID * jamId + listPosition,
    jamId,
    sheet_id
)

const val MULTIPLIER_JAM_ID = 10000L
