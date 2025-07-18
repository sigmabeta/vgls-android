package com.vgleadsheets.database.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.android.dao.AlternateSettingRoomDao
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.android.dao.FavoriteComposerRoomDao
import com.vgleadsheets.database.android.dao.FavoriteGameRoomDao
import com.vgleadsheets.database.android.dao.FavoriteSongRoomDao
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.android.dao.OfflineComposerRoomDao
import com.vgleadsheets.database.android.dao.OfflineSongRoomDao
import com.vgleadsheets.database.android.dao.SearchHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.android.dao.TagValuePlayCountRoomDao
import com.vgleadsheets.database.android.entity.AlternateSettingEntity
import com.vgleadsheets.database.android.entity.ComposerPlayCountEntity
import com.vgleadsheets.database.android.entity.FavoriteComposerEntity
import com.vgleadsheets.database.android.entity.FavoriteGameEntity
import com.vgleadsheets.database.android.entity.FavoriteSongEntity
import com.vgleadsheets.database.android.entity.GamePlayCountEntity
import com.vgleadsheets.database.android.entity.OfflineComposerEntity
import com.vgleadsheets.database.android.entity.OfflineSongEntity
import com.vgleadsheets.database.android.entity.SearchHistoryEntryEntity
import com.vgleadsheets.database.android.entity.SongHistoryEntryEntity
import com.vgleadsheets.database.android.entity.SongPlayCountEntity
import com.vgleadsheets.database.android.entity.TagValuePlayCountEntity

@Database(
    entities = [
        SongHistoryEntryEntity::class,
        GamePlayCountEntity::class,
        ComposerPlayCountEntity::class,
        TagValuePlayCountEntity::class,
        SongPlayCountEntity::class,
        SearchHistoryEntryEntity::class,
        FavoriteSongEntity::class,
        FavoriteGameEntity::class,
        FavoriteComposerEntity::class,
        OfflineSongEntity::class,
        OfflineComposerEntity::class,
        AlternateSettingEntity::class,
    ],
    version = UserContentDatabaseVersions.ADDED_OFFLINE_COMPOSERS,
)
abstract class UserContentDatabase : RoomDatabase() {
    abstract fun songHistoryEntryDao(): SongHistoryEntryRoomDao
    abstract fun gamePlayCountDao(): GamePlayCountRoomDao
    abstract fun composerPlayCountDao(): ComposerPlayCountRoomDao
    abstract fun tagValuePlayCountDao(): TagValuePlayCountRoomDao
    abstract fun songPlayCountDao(): SongPlayCountRoomDao
    abstract fun searchHistoryDao(): SearchHistoryEntryRoomDao
    abstract fun favoriteSongDao(): FavoriteSongRoomDao
    abstract fun favoriteGameDao(): FavoriteGameRoomDao
    abstract fun favoriteComposerDao(): FavoriteComposerRoomDao
    abstract fun offlineSongDao(): OfflineSongRoomDao
    abstract fun offlineComposerDao(): OfflineComposerRoomDao
    abstract fun alternateSettingDao(): AlternateSettingRoomDao
}
