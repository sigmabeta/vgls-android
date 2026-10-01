package com.vgleadsheets.jvm.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.UserContentDatabase
import com.vgleadsheets.database.android.VglsDatabase
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import net.sigmabeta.sage.di.AppScope
import java.io.File

/**
 * JVM Room database builders (BundledSQLiteDriver). Fresh desktop DBs are created at the current
 * schema. The android migrations use SupportSQLiteDatabase, so the desktop has its own
 * [SQLiteConnection] ones, starting from the schema the first desktop builds created (version
 * [DatabaseVersions.ADDED_SONG_MODIFIED_TIMES]). The DAO provisions live in the shared DatabaseModule.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmDatabaseModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideVglsDatabase(@Named("workDir") workDir: File): VglsDatabase = Room
        .databaseBuilder<VglsDatabase>(name = File(workDir, "vgls-database").absolutePath)
        .setDriver(BundledSQLiteDriver())
        .addMigrations(AddIgdbImageIds)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    private object AddIgdbImageIds : Migration(
        DatabaseVersions.ADDED_SONG_MODIFIED_TIMES,
        DatabaseVersions.ADDED_IGDB_IMAGE_IDS,
    ) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(DatabaseVersions.ADD_IGDB_IMAGE_ID_SQL)
        }
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideUserContentDatabase(@Named("workDir") workDir: File): UserContentDatabase = Room
        .databaseBuilder<UserContentDatabase>(name = File(workDir, "user-content-database").absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
