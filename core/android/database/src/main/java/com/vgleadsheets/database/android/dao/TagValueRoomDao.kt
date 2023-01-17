package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValueRoomDao :
    ManyFromOneDao<TagValueEntity> {
    @Query(QUERY_RELATED)
    override fun getEntitiesForForeign(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_RELATED)
    override fun getEntitiesForForeignSync(id: Long): List<TagValueEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<TagValueEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): TagValueEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<TagValueEntity>>

    @Insert
    override fun insert(entities: List<TagValueEntity>)

    @Delete(entity = TagValueEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Insert
    fun insertJoins(joins: List<SongTagValueJoin>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = TagValueEntity.TABLE

        private const val ROW_MANY_KEY = TagValueEntity.ROW_FOREIGN_KEY

        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_RELATED =
            "$GET $TABLE $WHERE_MANY $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
