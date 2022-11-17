package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValuesForSongDao : JoinDao<TagValueEntity> {
    @Query(QUERY_JOIN)
    override fun getJoinedEntities(id: Long): Flow<List<TagValueEntity>>

    @Query(QUERY_JOIN)
    override fun getJoinedEntitiesSync(id: Long): List<TagValueEntity>

    companion object {

        // Query Properties

        private const val TABLE = TagValueEntity.TABLE
        private const val TABLE_JOIN = SongTagValueJoin.TABLE

        private const val ROW_JOIN_ID_ONE = "$TABLE_JOIN.${SongTagValueJoin.ROW_FOREIGN_KEY_ONE}"
        private const val ROW_JOIN_ID_MANY = "$TABLE_JOIN.${SongTagValueJoin.ROW_FOREIGN_KEY_TWO}"

        private const val WHERE_JOIN =
            "INNER JOIN $TABLE_JOIN ON $TABLE.$ROW_PRIMARY_KEY_ID = $ROW_JOIN_ID_MANY " +
                "WHERE $ROW_JOIN_ID_ONE = :$ROW_PRIMARY_KEY_ID"

        // Bespoke Queries

        const val QUERY_JOIN =
            "$GET $TABLE $WHERE_JOIN $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
    }
}
