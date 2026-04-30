package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameAliasRoomDao : RoomDao<GameAliasEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<GameAliasEntity>>

    @Query(QUERY_GAME)
    fun getForGame(id: Long): Flow<List<GameAliasEntity>>

    @Query(QUERY_GAME)
    fun getForGameSync(id: Long): List<GameAliasEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<GameAliasEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): GameAliasEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<GameAliasEntity>>

    @Insert
    override fun insert(entities: List<GameAliasEntity>)

    @Delete(entity = GameAliasEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = GameAliasEntity.TABLE

        private const val COLUMN_FOREIGN_KEY_GAME = GameEntity.COLUMN_FOREIGN_KEY_ALIAS
        private const val WHERE_GAME = "$WHERE $COLUMN_FOREIGN_KEY_GAME = :$COLUMN_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_GAME =
            "$GET $TABLE $WHERE_GAME $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
