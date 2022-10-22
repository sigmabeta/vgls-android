package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.join.SongTagValueJoin
import com.vgleadsheets.database.join.SongTagValueJoin.Companion.ROW_FOREIGN_KEY_ONE
import com.vgleadsheets.database.join.SongTagValueJoin.Companion.ROW_FOREIGN_KEY_TWO
import kotlinx.coroutines.flow.Flow

@Dao
interface SongRoomDao :
    ManyFromOneDao<SongEntity>,
    ManyToManyDao<SongEntity, SongTagValueJoin> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<SongEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeign(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeignSync(id: Long): List<SongEntity>

    @Query(QUERY_JOIN)
    override fun getJoinedEntities(id: Long): Flow<List<SongEntity>>

    @Query(QUERY_JOIN)
    override fun getJoinedEntitiesSync(id: Long): List<SongEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<SongEntity>>

    @Insert
    override suspend fun insert(models: List<SongEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SongEntity.TABLE
        private const val TABLE_JOIN = SongTagValueJoin.TABLE

        private const val ROW_MANY_KEY = SongEntity.ROW_FOREIGN_KEY
        private const val ROW_JOIN_ID_LOCAL = "$TABLE_JOIN.$ROW_FOREIGN_KEY_ONE"
        private const val ROW_JOIN_ID_FOREIGN = "$TABLE_JOIN.$ROW_FOREIGN_KEY_TWO"

        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"
        private const val WHERE_JOIN =
            "INNER JOIN $TABLE_JOIN ON $TABLE.$ROW_PRIMARY_KEY_ID = $ROW_JOIN_ID_LOCAL WHERE $ROW_JOIN_ID_FOREIGN = :$ROW_PRIMARY_KEY_ID"

        private const val OPTION_ORDER_CUSTOM = "ORDER BY name, gameName"

        // Bespoke Queries

        const val QUERY_MANY = "$GET $TABLE $WHERE_MANY $OPTION_ORDER_CUSTOM"
        const val QUERY_JOIN =
            "$GET $TABLE $WHERE_JOIN $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
