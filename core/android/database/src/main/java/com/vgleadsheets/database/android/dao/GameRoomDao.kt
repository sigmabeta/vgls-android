package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRoomDao : RoomDao<GameEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<GameEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<GameEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): GameEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entities: List<GameEntity>)

    @Delete(entity = GameEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_INCREMENT)
    fun incrementSheetsPlayed(id: Long)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {
        private const val TABLE = GameEntity.TABLE
        private const val COLUMN_INCREMENTABLE = "sheetsPlayed"

        private const val SET_INCREMENT = "SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
        const val QUERY_INCREMENT = "UPDATE $TABLE $SET_INCREMENT $WHERE_SINGLE"
    }
}
