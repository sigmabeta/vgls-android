package com.vgleadsheets.database.android

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_OFFLINE
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity

object Migrations {
    object AddFavorites : Migration(
        DatabaseVersions.ADDED_PLAY_COUNTS,
        DatabaseVersions.ADDED_FAVORITES,
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN $COLUMN_FAVORITE INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN $COLUMN_OFFLINE INTEGER NOT NULL DEFAULT 0")
        }
    }
}
