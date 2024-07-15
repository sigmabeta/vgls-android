package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamePlayCountRoomDao {
    @Query(QUERY_ALL)
    fun getAll(): Flow<List<GamePlayCountEntity>>

    @Query(QUERY_UPSERT)
    suspend fun incrementPlayCount(id: Long)

    companion object {
        private const val DEFAULT_PLAY_COUNT = 1

        private const val TABLE = GamePlayCountEntity.TABLE
        private const val COLUMN_ID = "id"
        private const val COLUMN_INCREMENTABLE = "playCount"
        private const val LIST_ARGS = "(:id, $DEFAULT_PLAY_COUNT)"

        private const val QUERY_ALL = "$GET $TABLE"
        private const val QUERY_INSERT = "INSERT INTO $TABLE VALUES $LIST_ARGS"

        private const val ON_CONFLICT = "ON CONFLICT(`$COLUMN_ID`) DO"
        private const val SET_INCREMENT = "SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        private const val QUERY_INCREMENT = "$UPDATE $SET_INCREMENT"
        private const val QUERY_UPSERT = "$QUERY_INSERT $ON_CONFLICT $QUERY_INCREMENT"
    }
}
