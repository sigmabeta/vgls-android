package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.FavoriteSongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSongRoomDao {
    @Insert
    suspend fun insert(entity: FavoriteSongEntity)

    @Delete(entity = FavoriteSongEntity::class)
    suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_SINGLE)
    fun getFavoriteSong(id: Long): Flow<FavoriteSongEntity?>

    @Query(QUERY_ALL)
    fun getAll(): Flow<List<FavoriteSongEntity>>

    companion object {
        private const val TABLE = FavoriteSongEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE"
    }
}
