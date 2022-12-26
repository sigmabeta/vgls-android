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
    object RemoveJams : Migration(
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
            database.execSQL("$MIGRATE ${GameEntity.TABLE} $ADD $COLUMN_FAVORITE $WITH_DEFAULT_ZERO")
            database.execSQL("$MIGRATE ${GameEntity.TABLE} $ADD $COLUMN_OFFLINE $WITH_DEFAULT_ZERO")

            database.execSQL("$MIGRATE ${SongEntity.TABLE} $ADD $COLUMN_FAVORITE $WITH_DEFAULT_ZERO")
            database.execSQL("$MIGRATE ${SongEntity.TABLE} $ADD $COLUMN_OFFLINE $WITH_DEFAULT_ZERO")

            database.execSQL("$MIGRATE ${ComposerEntity.TABLE} $ADD $COLUMN_FAVORITE $WITH_DEFAULT_ZERO")
            database.execSQL("$MIGRATE ${ComposerEntity.TABLE} $ADD $COLUMN_OFFLINE $WITH_DEFAULT_ZERO")
        }
    }

    object AddAlternates : Migration(
        DatabaseVersions.ADDED_FAVORITES,
        DatabaseVersions.ADDED_ALTERNATES,
    ) {
        @Suppress("MaxLineLength")
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("$MIGRATE ${SongEntity.TABLE} $ADD altPageCount $WITH_DEFAULT_ZERO")
            database.execSQL("$MIGRATE ${SongEntity.TABLE} $ADD isAltSelected $WITH_DEFAULT_ZERO")
        }
    }

    const val DELETE_JAMS = "${RoomDao.DROP} jam"
    const val DELETE_SETLIST = "${RoomDao.DROP} setlist_entry"
    const val DELETE_SONG_HISTORY = "${RoomDao.DROP} song_history_entry"

    const val MIGRATE = "ALTER TABLE"
    const val ADD = "ADD COLUMN"
    const val WITH_DEFAULT_ZERO = "INTEGER NOT NULL DEFAULT 0"
}
