package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.AND
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlayCountRoomDao {
    @Query(QUERY_MOST_PLAYS)
    fun getMostPlays(): Flow<List<GamePlayCountEntity>>

    @Query(QUERY_UPSERT)
    suspend fun incrementPlayCount(id: Long, mostRecentPlay: Long)

    companion object {
        private const val DEFAULT_PLAY_COUNT = 1

        private const val TABLE = GamePlayCountEntity.TABLE
        private const val COLUMN_ID = "id"
        private const val COLUMN_INCREMENTABLE = "playCount"
        private const val COLUMN_MOST_RECENT = "mostRecentPlay"
        private const val LIST_ARGS = "(:id, $DEFAULT_PLAY_COUNT, :mostRecentPlay)"

        private const val BY_PLAY_COUNT = "ORDER BY playCount DESC"
        private const val NUM_RECORDS = "LIMIT 10"
        private const val ON_CONFLICT = "ON CONFLICT(`$COLUMN_ID`) DO"
        private const val SET_INCREMENT = "$COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"
        private const val SET_NEW_DATE = "$COLUMN_MOST_RECENT = :mostRecentPlay"

        private const val QUERY_INSERT = "INSERT INTO $TABLE VALUES $LIST_ARGS"
        private const val QUERY_MOST_PLAYS = "$GET $TABLE $BY_PLAY_COUNT $NUM_RECORDS"
        private const val QUERY_UPSERT = "$QUERY_INSERT $ON_CONFLICT $UPDATE $SET $SET_INCREMENT $AND $SET_NEW_DATE"
    }
}
