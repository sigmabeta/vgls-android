package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.FavoriteComposerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteComposerRoomDao {
    @Insert
    suspend fun insert(entity: FavoriteComposerEntity)

    @Delete(entity = FavoriteComposerEntity::class)
    suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_SINGLE)
    fun getFavoriteComposer(id: Long): Flow<FavoriteComposerEntity?>

    companion object {
        private const val TABLE = FavoriteComposerEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
    }
}
