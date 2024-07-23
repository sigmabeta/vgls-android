package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongHistoryEntryRoomDao {
    @Insert
    suspend fun insert(entity: SongHistoryEntryEntity)

    @Query(QUERY_MOST_PLAYS)
    fun getMostPlays(): Flow<List<SongHistoryEntryEntity>>

    companion object {
        private const val TABLE = SongHistoryEntryEntity.TABLE
        private const val COLUMN_ID = "id"

        private const val BY_TIMESTAMP = "ORDER BY timeMs DESC"
        private const val NUM_RECORDS = "LIMIT 10"

        private const val QUERY_MOST_PLAYS = "$GET $TABLE $BY_TIMESTAMP $NUM_RECORDS"
    }
}
