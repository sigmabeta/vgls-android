package com.vgleadsheets.database.android

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_OFFLINE
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity

object Migrations {
    object RemovedJams : Migration(
        DatabaseVersions.ADDED_PLAY_COUNTS,
        DatabaseVersions.REMOVED_JAMS
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(DELETE_JAMS)
            database.execSQL(DELETE_SETLIST)
            database.execSQL(DELETE_SONG_HISTORY)
        }
    }

    object AddFavorites : Migration(
        DatabaseVersions.REMOVED_JAMS,
        DatabaseVersions.ADDED_FAVORITES,
    ) {
        @Suppress("MaxLineLength")
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")
        }
    }

    const val DELETE_JAMS = "${RoomDao.DROP} jam"
    const val DELETE_SETLIST = "${RoomDao.DROP} setlist_entry"
    const val DELETE_SONG_HISTORY = "${RoomDao.DROP} song_history_entry"
}
