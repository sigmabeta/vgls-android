package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagKeyRoomDao : RoomDao<TagKeyEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<TagKeyEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<TagKeyEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): TagKeyEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<TagKeyEntity>>

    @Insert
    override suspend fun insert(entities: List<TagKeyEntity>)

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = "tag_key"

        // Bespoke Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
