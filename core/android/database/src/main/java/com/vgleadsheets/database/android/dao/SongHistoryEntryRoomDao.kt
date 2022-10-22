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
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongHistoryEntryRoomDao : ManyFromOneDao<SongHistoryEntryEntity> {
    fun getAllForJamSync(jamId: Long): List<SongHistoryEntryEntity>

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
    override suspend fun insert(models: List<SongHistoryEntryEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SongHistoryEntryEntity.TABLE

        private const val ROW_MANY_KEY = SongHistoryEntryEntity.ROW_FOREIGN_KEY
        private const val WHERE_MANY = "WHERE $ROW_MANY_KEY = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_MANY =
            "$GET $TABLE $WHERE_MANY $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}

