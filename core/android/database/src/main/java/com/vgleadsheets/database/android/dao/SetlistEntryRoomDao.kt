package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.SetlistEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetlistEntryRoomDao : OneToOneDao<SetlistEntryEntity> {
    @Query(QUERY_RELATED)
    fun getSetlistEntriesForJam(id: Long): Flow<List<SetlistEntryEntity>>

    @Query(QUERY_FOREIGN)
    override fun getForeignEntity(id: Long): Flow<SetlistEntryEntity>

    @Query(QUERY_FOREIGN)
    override fun getForeignEntitySync(id: Long): SetlistEntryEntity

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SetlistEntryEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SetlistEntryEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<SetlistEntryEntity>>

    @Insert
    override fun insert(entities: List<SetlistEntryEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SetlistEntryEntity.TABLE

        private const val ROW_FOREIGN_KEY = SetlistEntryEntity.ROW_FOREIGN_KEY
        private const val WHERE_FOREIGN = "WHERE $ROW_FOREIGN_KEY = :$ROW_PRIMARY_KEY_ID"

        private const val OPTION_ORDER_ID = "ORDER BY $ROW_PRIMARY_KEY_ID"

        private const val ROW_MANY_KEY = SetlistEntryEntity.ROW_FOREIGN_KEY

        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_FOREIGN =
            "$GET $TABLE $WHERE_FOREIGN $OPTION_ORDER_ID $OPTION_CASE_INSENSITIVE"
        const val QUERY_RELATED =
            "$GET $TABLE $WHERE_MANY"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ORDER_ID $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
