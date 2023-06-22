package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.INNER_JOIN
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ON
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
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
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.android.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface SongRoomDao : RoomDao<SongEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<SongEntity>>

    @Query(QUERY_FOR_GAME)
    fun getForGame(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_FOR_GAME)
    fun getForGameSync(id: Long): List<SongEntity>

    @Query(QUERY_FOR_COMPOSER)
    fun getForComposer(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_FOR_COMPOSER)
    fun getForComposerSync(id: Long): List<SongEntity>

    @Query(QUERY_FOR_TAG_VALUE)
    fun getForTagValue(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_FOR_TAG_VALUE)
    fun getForTagValueSync(id: Long): List<SongEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<SongEntity>>

    @Insert
    override fun insert(entities: List<SongEntity>)

    @Delete(entity = SongEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_FAVORITES)
    fun getFavorites(): Flow<List<SongEntity>>

    @Query(QUERY_INCREMENT)
    fun incrementPlayCount(id: Long)

    @Query(QUERY_TOGGLE_FAVORITE)
    fun toggleFavorite(id: Long)

    @Query(QUERY_TOGGLE_OFFLINE)
    fun toggleOffline(id: Long)

    @Query(QUERY_TOGGLE_ALTERNATE)
    fun toggleAlternate(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJoins(joins: List<SongTagValueJoin>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties
        private const val TABLE = SongEntity.TABLE
        private const val TABLE_COMPOSER = SongComposerJoin.TABLE
        private const val TABLE_TAG_VALUE = SongTagValueJoin.TABLE

        private const val COLUMN_FOREIGN_KEY_GAME = GameEntity.COLUMN_FOREIGN_KEY
        private const val COLUMN_FOREIGN_KEY_COMPOSER = SongComposerJoin.COLUMN_FOREIGN_KEY_TWO
        private const val COLUMN_FOREIGN_KEY_COMPOSER_JOIN = SongComposerJoin.COLUMN_FOREIGN_KEY_ONE
        private const val COLUMN_FOREIGN_KEY_TAG_VALUE = SongTagValueJoin.COLUMN_FOREIGN_KEY_ONE
        private const val COLUMN_FOREIGN_KEY_TAG_VALUE_JOIN = SongTagValueJoin.COLUMN_FOREIGN_KEY_TWO

        private const val WHERE_GAME = "$WHERE $COLUMN_FOREIGN_KEY_GAME = :$COLUMN_PRIMARY_KEY_ID"
        private const val WHERE_COMPOSER = "$INNER_JOIN $TABLE_COMPOSER $ON $TABLE_COMPOSER.$COLUMN_FOREIGN_KEY_COMPOSER_JOIN = $COLUMN_PRIMARY_KEY_ID" +
            " $WHERE $COLUMN_FOREIGN_KEY_COMPOSER = :$COLUMN_PRIMARY_KEY_ID"
        private const val WHERE_TAG_VALUE = "$INNER_JOIN $TABLE_TAG_VALUE $ON $TABLE_TAG_VALUE.$COLUMN_FOREIGN_KEY_TAG_VALUE_JOIN = $COLUMN_PRIMARY_KEY_ID" +
            " $WHERE $COLUMN_FOREIGN_KEY_TAG_VALUE = :$COLUMN_PRIMARY_KEY_ID"

        private const val OPTION_ORDER_CUSTOM = "ORDER BY name, gameName"

        private const val COLUMN_INCREMENTABLE = "playCount"
        private const val COLUMN_ALTERNATE = "isAltSelected"

        private const val SET_INCREMENT = "$SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        private const val TOGGLE_ALTERNATE = "$SET $COLUMN_ALTERNATE = (1 - $COLUMN_ALTERNATE)"

        // Bespoke Queries

        const val QUERY_FOR_GAME = "$GET $TABLE $WHERE_GAME $OPTION_ORDER_CUSTOM"
        const val QUERY_FOR_COMPOSER = "$GET $TABLE $WHERE_COMPOSER $OPTION_ORDER_CUSTOM"
        const val QUERY_FOR_TAG_VALUE = "$GET $TABLE $WHERE_TAG_VALUE $OPTION_ORDER_CUSTOM"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
        const val QUERY_UPDATE = "$UPDATE $TABLE"
        const val QUERY_FAVORITES =
            "$GET $TABLE $WHERE_FAVORITE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        const val QUERY_INCREMENT = "$QUERY_UPDATE $SET_INCREMENT $WHERE_SINGLE"

        const val QUERY_TOGGLE_FAVORITE = "$QUERY_UPDATE $TOGGLE_FAVORITE $WHERE_SINGLE"
        const val QUERY_TOGGLE_OFFLINE = "$QUERY_UPDATE $TOGGLE_OFFLINE $WHERE_SINGLE"
        const val QUERY_TOGGLE_ALTERNATE = "$QUERY_UPDATE $TOGGLE_ALTERNATE $WHERE_SINGLE"
    }
}
