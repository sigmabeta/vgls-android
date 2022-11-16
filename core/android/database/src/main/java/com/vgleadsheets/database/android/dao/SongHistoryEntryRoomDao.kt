package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongHistoryEntryRoomDao : ManyFromOneDao<SongHistoryEntryEntity> {
    @Query(QUERY_RELATED)
    fun getSongHistoryEntriesForJam(id: Long): Flow<List<SongHistoryEntryEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeign(id: Long): Flow<List<SongHistoryEntryEntity>>

    @Query(QUERY_MANY)
    override fun getEntitiesForForeignSync(id: Long): List<SongHistoryEntryEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongHistoryEntryEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongHistoryEntryEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<SongHistoryEntryEntity>>

    @Insert
    override fun insert(entities: List<SongHistoryEntryEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SongHistoryEntryEntity.TABLE

        private const val ROW_MANY_KEY = SongHistoryEntryEntity.ROW_FOREIGN_KEY
        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        private const val OPTION_ORDER_ID = "ORDER BY $ROW_PRIMARY_KEY_ID"

        private const val WHERE_RELATED = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_MANY =
            "$GET $TABLE $WHERE_MANY $OPTION_ORDER_ID $OPTION_CASE_INSENSITIVE"
        const val QUERY_RELATED =
            "$GET $TABLE $WHERE_RELATED"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ORDER_ID $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}

