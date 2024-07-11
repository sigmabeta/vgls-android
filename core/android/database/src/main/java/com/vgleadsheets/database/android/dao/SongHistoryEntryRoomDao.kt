package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity.Companion.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface SongHistoryEntryRoomDao {
    @Insert
    suspend fun insert(entity: SongHistoryEntryEntity)

    @Query(QUERY_ALL)
    fun getAll(): Flow<List<SongHistoryEntryEntity>>

    companion object {
        const val QUERY_ALL = "$GET $TABLE"
    }
}
