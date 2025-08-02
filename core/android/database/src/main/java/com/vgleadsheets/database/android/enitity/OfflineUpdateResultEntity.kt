package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.OfflineUpdateResultEntity.Companion.TABLE

@Suppress("ConstructorParameterNaming")
@Entity(tableName = TABLE)
data class OfflineUpdateResultEntity(
    @PrimaryKey val id: Int,
    val date_time: String, // in ISO8601 format
    val server_update_time: String, // in ISO8601 format
    val new_songs: Int,
    val new_games: Int,
    val new_composers: Int,
    val updated_songs: Int,
    val successful_offlines: Int,
) {

    companion object {
        const val TABLE = "offline_update"
    }
}
