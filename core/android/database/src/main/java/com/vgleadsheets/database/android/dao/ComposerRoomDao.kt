package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposerRoomDao :
    ManyToManyDao<ComposerEntity, SongComposerJoin> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<ComposerEntity>>

    @Query(QUERY_JOIN)
    override fun getJoinedEntities(id: Long): Flow<List<ComposerEntity>>

    @Query(QUERY_JOIN)
    override fun getJoinedEntitiesSync(id: Long): List<ComposerEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<ComposerEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): ComposerEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<ComposerEntity>>

    @Insert
    override suspend fun insert(models: List<ComposerEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = ComposerEntity.TABLE
        private const val TABLE_JOIN = SongComposerJoin.TABLE

        private const val ROW_JOIN_ID_LOCAL = "$TABLE_JOIN.${SongComposerJoin.ROW_FOREIGN_KEY_TWO}"
        private const val ROW_JOIN_ID_FOREIGN =
            "$TABLE_JOIN.${SongComposerJoin.ROW_FOREIGN_KEY_ONE}"

        private const val WHERE_JOIN =
            "INNER JOIN $TABLE_JOIN ON $TABLE.$ROW_PRIMARY_KEY_ID = $ROW_JOIN_ID_LOCAL WHERE $ROW_JOIN_ID_FOREIGN = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

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
