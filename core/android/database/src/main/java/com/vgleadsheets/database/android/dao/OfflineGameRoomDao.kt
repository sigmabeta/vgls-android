package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.OfflineGameEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineGameRoomDao {
    @Insert
    suspend fun insert(entity: OfflineGameEntity)

    @Delete(entity = OfflineGameEntity::class)
    suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_SINGLE)
    fun getOfflineGame(id: Long): Flow<OfflineGameEntity?>

    @Query(QUERY_ALL)
    fun getAll(): Flow<List<OfflineGameEntity>>

    companion object {
        private const val TABLE = OfflineGameEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE"
    }
}
