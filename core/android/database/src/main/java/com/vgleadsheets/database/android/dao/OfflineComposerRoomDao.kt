package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.OfflineComposerEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineComposerRoomDao {
    @Insert
    suspend fun insert(entity: OfflineComposerEntity)

    @Delete(entity = OfflineComposerEntity::class)
    suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_SINGLE)
    fun getOfflineComposer(id: Long): Flow<OfflineComposerEntity?>

    @Query(QUERY_ALL)
    fun getAll(): Flow<List<OfflineComposerEntity>>

    companion object {
        private const val TABLE = OfflineComposerEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE"
    }
}
