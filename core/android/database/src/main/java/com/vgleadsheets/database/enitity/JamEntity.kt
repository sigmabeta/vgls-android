package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry
import com.vgleadsheets.network.model.ApiJam

@Entity(tableName = "jam")
data class JamEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val currentSheetId: Long?
)

fun JamEntity.toJam(
    currentSong: Song?,
    songHistory: List<SongHistoryEntry>?
) = Jam(
    id,
    name,
    currentSong,
    songHistory
)

fun ApiJam.converter(name: String) = JamEntity(
    jam_id,
    name,
    song_history
    .firstOrNull()?.sheet_id
)
