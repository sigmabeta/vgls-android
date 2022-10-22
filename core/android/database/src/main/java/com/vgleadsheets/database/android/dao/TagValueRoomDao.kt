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
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.database.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValueRoomDao :
    ManyFromOneDao<TagValueEntity>,
    ManyToManyDao<TagValueEntity, SongTagValueJoin> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<TagValueEntity>>

    @Query(QUERY_RELATED)
    override fun getEntitiesForForeign(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_RELATED)
    override fun getEntitiesForForeignSync(id: Long): List<TagValueEntity>

    @Query(QUERY_JOIN)
    override fun getJoinedEntities(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_JOIN)
    override fun getJoinedEntitiesSync(id: Long): List<TagValueEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<TagValueEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): TagValueEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<TagValueEntity>>

    @Insert
    override suspend fun insert(models: List<TagValueEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = TagValueEntity.TABLE
        private const val TABLE_JOIN = SongTagValueJoin.TABLE

        private const val ROW_MANY_KEY = TagValueEntity.ROW_FOREIGN_KEY
        private const val ROW_JOIN_ID_LOCAL = "$TABLE_JOIN.${SongTagValueJoin.ROW_FOREIGN_KEY_TWO}"
        private const val ROW_JOIN_ID_FOREIGN =
            "$TABLE_JOIN.${SongTagValueJoin.ROW_FOREIGN_KEY_ONE}"

        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"
        private const val WHERE_JOIN =
            "INNER JOIN $TABLE_JOIN ON $TABLE.$ROW_PRIMARY_KEY_ID = $ROW_JOIN_ID_LOCAL WHERE $ROW_JOIN_ID_FOREIGN = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_RELATED =
            "$GET $TABLE $WHERE_MANY $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
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
