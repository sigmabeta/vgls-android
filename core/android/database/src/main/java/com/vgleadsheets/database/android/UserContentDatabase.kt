package com.vgleadsheets.database.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.android.dao.FavoriteComposerRoomDao
import com.vgleadsheets.database.android.dao.FavoriteGameRoomDao
import com.vgleadsheets.database.android.dao.FavoriteSongRoomDao
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.android.dao.SearchHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.android.dao.TagValuePlayCountRoomDao
import com.vgleadsheets.database.android.enitity.ComposerPlayCountEntity
import com.vgleadsheets.database.android.enitity.FavoriteComposerEntity
import com.vgleadsheets.database.android.enitity.FavoriteGameEntity
import com.vgleadsheets.database.android.enitity.FavoriteSongEntity
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity
import com.vgleadsheets.database.android.enitity.SearchHistoryEntryEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import com.vgleadsheets.database.android.enitity.SongPlayCountEntity
import com.vgleadsheets.database.android.enitity.TagValuePlayCountEntity

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
    ],
    version = UserContentDatabaseVersions.ORIGINAL,
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
}
