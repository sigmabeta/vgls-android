package com.vgleadsheets.database.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.enitity.ComposerPlayCountEntity
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity

@Database(
    entities = [
        SongHistoryEntryEntity::class,
        GamePlayCountEntity::class,
        ComposerPlayCountEntity::class,
    ],
    version = UserContentDatabaseVersions.ORIGINAL,
)
abstract class UserContentDatabase : RoomDatabase() {
    abstract fun songHistoryEntryDao(): SongHistoryEntryRoomDao
    abstract fun gamePlayCountDao(): GamePlayCountRoomDao
    abstract fun composerPlayCountDao(): ComposerPlayCountRoomDao
}
