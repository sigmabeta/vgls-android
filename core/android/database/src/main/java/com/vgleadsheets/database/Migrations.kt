package com.vgleadsheets.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DROP

object Migrations {
    val REMOVED_JAMS = object : Migration(
        DatabaseVersions.ADDED_PLAY_COUNTS,
        DatabaseVersions.REMOVED_JAMS
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(DELETE_JAMS)
            database.execSQL(DELETE_SETLIST)
            database.execSQL(DELETE_SONG_HISTORY)
        }
    }

    const val DELETE_JAMS = "$DROP jam"
    const val DELETE_SETLIST = "$DROP setlist_entry"
    const val DELETE_SONG_HISTORY = "$DROP song_history_entry"
}
