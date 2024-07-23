package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.enitity.ComposerPlayCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposerPlayCountRoomDao {
    @Query(QUERY_MOST_PLAYS)
    fun getMostPlays(): Flow<List<ComposerPlayCountEntity>>

    @Query(QUERY_UPSERT)
    suspend fun incrementPlayCount(id: Long, mostRecentPlay: Long)

    companion object {
        private const val DEFAULT_PLAY_COUNT = 1

        private const val TABLE = ComposerPlayCountEntity.TABLE
        private const val COLUMN_ID = "id"
        private const val COLUMN_INCREMENTABLE = "playCount"
        private const val LIST_ARGS = "(:id, $DEFAULT_PLAY_COUNT, :mostRecentPlay)"

        private const val BY_PLAY_COUNT = "ORDER BY playCount DESC"
        private const val NUM_RECORDS = "LIMIT 10"
        private const val ON_CONFLICT = "ON CONFLICT(`$COLUMN_ID`) DO"
        private const val SET_INCREMENT = "SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        private const val QUERY_INSERT = "INSERT INTO $TABLE VALUES $LIST_ARGS"
        private const val QUERY_MOST_PLAYS = "$GET $TABLE $BY_PLAY_COUNT $NUM_RECORDS"
        private const val QUERY_INCREMENT = "$UPDATE $SET_INCREMENT"
        private const val QUERY_UPSERT = "$QUERY_INSERT $ON_CONFLICT $QUERY_INCREMENT"
    }
}
