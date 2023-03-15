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
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_OFFLINE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.join.SongComposerJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposerRoomDao : RoomDao<ComposerEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<ComposerEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<ComposerEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): ComposerEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<ComposerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entities: List<ComposerEntity>)

    @Delete(entity = ComposerEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_INCREMENT)
    fun incrementSheetsPlayed(id: Long)

    @Query(QUERY_TOGGLE_FAVORITE)
    fun toggleFavorite(id: Long)

    @Query(QUERY_TOGGLE_OFFLINE)
    fun toggleOffline(id: Long)

    @Insert
    fun insertJoins(joins: List<SongComposerJoin>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = ComposerEntity.TABLE
        private const val COLUMN_INCREMENTABLE = "sheetsPlayed"
        private const val SET_INCREMENT = "$SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

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
