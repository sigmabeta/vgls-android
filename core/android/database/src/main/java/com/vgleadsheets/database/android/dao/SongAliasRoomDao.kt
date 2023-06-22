package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.SongAliasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongAliasRoomDao : RoomDao<SongAliasEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<SongAliasEntity>>

    @Query(QUERY_SONG)
    fun getForSong(id: Long): Flow<List<SongAliasEntity>>

    @Query(QUERY_SONG)
    fun getForSongSync(id: Long): List<SongAliasEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongAliasEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongAliasEntity

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<SongAliasEntity>>

    @Insert
    override fun insert(entities: List<SongAliasEntity>)

    @Delete(entity = SongAliasEntity::class)
    override fun remove(ids: List<DeletionId>)

    @Query(QUERY_DELETE)
    override fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = SongAliasEntity.TABLE

        private const val COLUMN_FOREIGN_KEY_SONG = SongAliasEntity.COLUMN_FOREIGN_KEY
        private const val WHERE_SONG = "$WHERE $COLUMN_FOREIGN_KEY_SONG = :$COLUMN_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_SONG =
            "$GET $TABLE $WHERE_SONG $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
