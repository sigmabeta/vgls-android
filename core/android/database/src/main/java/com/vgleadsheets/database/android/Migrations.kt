package com.vgleadsheets.database.android

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity

object Migrations {
    object AddFavorites: Migration(
        DatabaseVersions.ADDED_PLAY_COUNTS,
        DatabaseVersions.ADDED_FAVORITES,
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${GameEntity.TABLE} ADD COLUMN isAvailableOffline INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${SongEntity.TABLE} ADD COLUMN isAvailableOffline INTEGER NOT NULL DEFAULT 0")

            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE ${ComposerEntity.TABLE} ADD COLUMN isAvailableOffline INTEGER NOT NULL DEFAULT 0")
        }
    }
}
