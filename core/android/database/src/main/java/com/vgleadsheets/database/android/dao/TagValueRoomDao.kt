package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.INNER_JOIN
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ON
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValueRoomDao : RoomDao<TagValueEntity> {
    @Query(QUERY_TAG_KEY)
    fun getForTagKey(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_TAG_KEY)
    fun getForTagKeySync(id: Long): List<TagValueEntity>

    @Query(QUERY_SONG)
    fun getForSong(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_SONG)
    fun getForSongSync(id: Long): List<TagValueEntity>

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
        private const val TABLE_SONG = SongTagValueJoin.TABLE

        private const val COLUMN_FOREIGN_KEY_TAG_KEY = TagKeyEntity.COLUMN_FOREIGN_KEY
        private const val COLUMN_FOREIGN_KEY_SONG = SongTagValueJoin.COLUMN_FOREIGN_KEY_ONE
        private const val COLUMN_FOREIGN_KEY_SONG_JOIN = SongTagValueJoin.COLUMN_FOREIGN_KEY_TWO

        private const val WHERE_TAG_KEY =
            "WHERE $COLUMN_FOREIGN_KEY_TAG_KEY = :$COLUMN_PRIMARY_KEY_ID"
        private const val WHERE_SONG =
            "$INNER_JOIN $TABLE_SONG $ON $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG_JOIN = $COLUMN_PRIMARY_KEY_ID" +
                " $WHERE $COLUMN_FOREIGN_KEY_SONG = :$COLUMN_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_TAG_KEY =
            "$GET $TABLE $WHERE_TAG_KEY $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_SONG = "$GET $TABLE $WHERE_SONG $OPTION_ALPHABETICAL_ORDER"

        // Default Queries

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        const val QUERY_DELETE = "$DELETE $TABLE"
    }
}
