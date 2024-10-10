package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.SearchHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryEntryRoomDao {
    @Insert
    suspend fun insert(entity: SearchHistoryEntryEntity)

    @Delete(entity = SearchHistoryEntryEntity::class)
    suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_MOST_RECENT)
    fun getRecentEntries(): Flow<List<SearchHistoryEntryEntity>>

    companion object {
        private const val TABLE = SearchHistoryEntryEntity.TABLE

        private const val BY_TIMESTAMP = "ORDER BY timeMs DESC"
        private const val NUM_RECORDS = "LIMIT 10"

        const val QUERY_MOST_RECENT = "$GET $TABLE $BY_TIMESTAMP $NUM_RECORDS"
    }
}
