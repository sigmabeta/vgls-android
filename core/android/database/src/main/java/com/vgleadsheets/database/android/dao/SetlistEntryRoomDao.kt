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
import com.vgleadsheets.database.enitity.SetlistEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetlistEntryRoomDao : OneToOneDao<SetlistEntryEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<SetlistEntryEntity>>

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
    override suspend fun insert(models: List<SetlistEntryEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SetlistEntryEntity.TABLE

        private const val ROW_FOREIGN_KEY = SetlistEntryEntity.ROW_FOREIGN_KEY
        private const val WHERE_FOREIGN = "WHERE $ROW_FOREIGN_KEY = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_FOREIGN =
            "$GET $TABLE $WHERE_FOREIGN $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
