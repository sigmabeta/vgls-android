package com.vgleadsheets.database.android

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object UserContentMigrations {
    object AddedOffline : Migration(
        UserContentDatabaseVersions.ADDED_OFFLINE_SONGS,
        UserContentDatabaseVersions.ADDED_OFFLINE_COMPOSERS
    ) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(AddedOfflineSqlStatements.CREATE_OFFLINE_SONGS)

            database.execSQL(AddedOfflineSqlStatements.CREATE_OFFLINE_COMPOSERS)

            // TODO
            // database.execSQL(AddedOfflineSqlStatements.CREATE_OFFLINE_GAMES)
        }
    }

    @Suppress("MaxLineLength")
    private object AddedOfflineSqlStatements {
        // Copied from generated code in `UserContentDatabase_Impl.kt`
        const val CREATE_OFFLINE_SONGS = "CREATE TABLE IF NOT EXISTS `offline_song` (`id` INTEGER NOT NULL, PRIMARY KEY(`id`))"

        const val CREATE_OFFLINE_COMPOSERS = "CREATE TABLE IF NOT EXISTS `offline_composer` (`id` INTEGER NOT NULL, PRIMARY KEY(`id`))"

        // TODO
        // const val CREATE_OFFLINE_GAMES = "CREATE TABLE IF NOT EXISTS `offline_game` (`id` INTEGER NOT NULL, PRIMARY KEY(`id`))"
    }
}
