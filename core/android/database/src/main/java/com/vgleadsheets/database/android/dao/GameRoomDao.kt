package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.enitity.GameEntity
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

    @Insert
    override suspend fun insert(models: List<GameEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {
        private const val TABLE = GameEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
