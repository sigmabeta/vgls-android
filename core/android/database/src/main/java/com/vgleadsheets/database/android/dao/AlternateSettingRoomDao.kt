package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.AlternateSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlternateSettingRoomDao {
    @Query(QUERY_TOGGLE_ALTERNATE)
    fun toggleAlternate(id: Long)

    @Query(QUERY_SINGLE)
    fun getAlternateSetting(id: Long): Flow<AlternateSettingEntity?>

    companion object {
        private const val TABLE = AlternateSettingEntity.TABLE

        private const val COLUMN_ID = "id"
        private const val COLUMN_ALTERNATE = "isAltSelected"
        private const val LIST_ARGS = "(:id, 1)"

        private const val TOGGLE_ALTERNATE = "$COLUMN_ALTERNATE = (1 - $COLUMN_ALTERNATE)"
        private const val ON_CONFLICT = "ON CONFLICT(`$COLUMN_ID`) DO"

        private const val QUERY_INSERT = "INSERT INTO $TABLE VALUES $LIST_ARGS"
        private const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        private const val QUERY_TOGGLE_ALTERNATE = "$QUERY_INSERT $ON_CONFLICT $UPDATE $SET $TOGGLE_ALTERNATE"
    }
}
