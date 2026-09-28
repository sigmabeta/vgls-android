package com.vgleadsheets.database.android.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.vgleadsheets.database.android.DatabaseVersions
import com.vgleadsheets.database.android.Migrations
import com.vgleadsheets.database.android.UserContentDatabase
import com.vgleadsheets.database.android.UserContentMigrations
import com.vgleadsheets.database.android.VglsDatabase
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.di.AppScope

/**
 * Android's Room database builders — the framework/Requery SupportSQLite open-helper path, with the
 * `SupportSQLiteDatabase` migrations. The JVM/desktop twin (BundledSQLiteDriver) is JvmDatabaseModule
 * in apps/jvm; the shared [DatabaseModule] provides the DAOs off whichever database binding resolves.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object AndroidDatabaseModule {
    @SingleIn(AppScope::class)
    @Provides
    fun provideSqlOpenHelperFactory(appInfo: AppInfo): SupportSQLiteOpenHelper.Factory = if (appInfo.isDebug) {
        FrameworkSQLiteOpenHelperFactory()
    } else {
        RequerySQLiteOpenHelperFactory()
    }

    @SingleIn(AppScope::class)
    @Provides
    @Suppress("SpreadOperator")
    fun provideVglsDatabase(
        context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory,
    ): VglsDatabase = Room
        .databaseBuilder(
            context,
            VglsDatabase::class.java,
            "vgls-database",
        )
        .openHelperFactory(sqlOpenHelperFactory)
        .addMigrations(
            Migrations.RemoveJams,
            Migrations.AddFavorites,
            Migrations.AddAlternates,
            Migrations.AddSongCounts,
            Migrations.AddOfflineUpdateResults,
            Migrations.AddSongModifiedTimes,
        )
        .fallbackToDestructiveMigrationFrom(dropAllTables = true, *DatabaseVersions.WITHOUT_MIGRATION)
        .build()

    @SingleIn(AppScope::class)
    @Provides
    fun provideUserContentDatabase(
        context: Context,
        sqlOpenHelperFactory: SupportSQLiteOpenHelper.Factory,
    ): UserContentDatabase = Room
        .databaseBuilder(
            context,
            UserContentDatabase::class.java,
            "user-content-database",
        )
        .addMigrations(
            UserContentMigrations.AddedOfflineSongs,
            UserContentMigrations.AddedOfflineComposers,
            UserContentMigrations.AddedOfflineGames,
        )
        .openHelperFactory(sqlOpenHelperFactory)
        .build()
}
