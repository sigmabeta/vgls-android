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
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_LIMIT
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_SONG_COUNT_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_OFFLINE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
@Suppress("TooManyFunctions")
interface GameRoomDao : RoomDao<GameEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<GameEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<GameEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): GameEntity

    @Query(QUERY_IDS)
    fun getByIdList(ids: Array<Long>): Flow<List<GameEntity>>

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entities: List<GameEntity>)

    @Delete(entity = GameEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_MOST_SONGS)
    fun getMostSongsGames(): Flow<List<GameEntity>>

    @Query(QUERY_FAVORITES)
    fun getFavorites(): Flow<List<GameEntity>>

    @Query(QUERY_INCREMENT)
    fun incrementSheetsPlayed(id: Long)

    @Query(QUERY_TOGGLE_FAVORITE)
    fun toggleFavorite(id: Long)

    @Query(QUERY_TOGGLE_OFFLINE)
    fun toggleOffline(id: Long)

    @Query(QUERY_HIGHEST_ID)
    fun getHighestId(): Flow<GameEntity>

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {
        private const val TABLE = GameEntity.TABLE
        private const val COLUMN_INCREMENTABLE = "sheetsPlayed"

        private const val WHERE_IDS = "$WHERE id in (:ids)"
        private const val SET_INCREMENT = "$SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        private const val OPTION_BY_ID = "ORDER BY id DESC"
        private const val OPTION_NUM_RECORDS_BY_ID = "LIMIT 1"

        private const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        private const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_IDS = "$GET $TABLE $WHERE_IDS"
        private const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_DELETE = "$DELETE $TABLE"
        private const val QUERY_UPDATE = "$UPDATE $TABLE"
        private const val QUERY_FAVORITES =
            "$GET $TABLE $WHERE_FAVORITE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_MOST_SONGS = "$GET $TABLE $OPTION_SONG_COUNT_ORDER $OPTION_LIMIT 15"

        private const val QUERY_INCREMENT = "$UPDATE $TABLE $SET_INCREMENT $WHERE_SINGLE"

        private const val QUERY_TOGGLE_FAVORITE = "$QUERY_UPDATE $TOGGLE_FAVORITE $WHERE_SINGLE"
        private const val QUERY_TOGGLE_OFFLINE = "$QUERY_UPDATE $TOGGLE_OFFLINE $WHERE_SINGLE"
        private const val QUERY_HIGHEST_ID = "$GET $TABLE $OPTION_BY_ID $OPTION_NUM_RECORDS_BY_ID"
    }
}
