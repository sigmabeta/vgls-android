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
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_OFFLINE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface SongRoomDao :
    ManyFromOneDao<SongEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<SongEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeign(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeignSync(id: Long): List<SongEntity>

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

    @Query(QUERY_INCREMENT)
    fun incrementPlayCount(id: Long)

    @Query(QUERY_TOGGLE_FAVORITE)
    fun toggleFavorite(id: Long)

    @Query(QUERY_TOGGLE_OFFLINE)
    fun toggleOffline(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJoins(joins: List<SongTagValueJoin>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SongEntity.TABLE

        private const val ROW_MANY_KEY = SongEntity.ROW_FOREIGN_KEY

        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        private const val OPTION_ORDER_CUSTOM = "ORDER BY name, gameName"

        private const val COLUMN_INCREMENTABLE = "playCount"
        private const val SET_INCREMENT = "$SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        // Bespoke Queries

        const val QUERY_MANY = "$GET $TABLE $WHERE_MANY $OPTION_ORDER_CUSTOM"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
        const val QUERY_UPDATE = "$UPDATE $TABLE"

        const val QUERY_INCREMENT = "$QUERY_UPDATE $SET_INCREMENT $WHERE_SINGLE"

        const val QUERY_TOGGLE_FAVORITE = "$QUERY_UPDATE $TOGGLE_FAVORITE $WHERE_SINGLE"
        const val QUERY_TOGGLE_OFFLINE = "$QUERY_UPDATE $TOGGLE_OFFLINE $WHERE_SINGLE"
    }
}
