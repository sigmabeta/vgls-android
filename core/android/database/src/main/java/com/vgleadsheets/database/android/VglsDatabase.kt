package com.vgleadsheets.database.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.ComposersForSongDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.JamRoomDao
import com.vgleadsheets.database.android.dao.SetlistEntryRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.SongsForComposerDao
import com.vgleadsheets.database.android.dao.SongsForTagValueDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.dao.TagValuesForSongDao
import com.vgleadsheets.database.android.dao.TransactionDao
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.JamEntity
import com.vgleadsheets.database.android.enitity.SetlistEntryEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.enitity.TimeEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.android.join.SongTagValueJoin

@Database(
    entities = [
        GameEntity::class,
        SongEntity::class,
        ComposerEntity::class,
        SongComposerJoin::class,
        SongTagValueJoin::class,
        TimeEntity::class,
        GameAliasEntity::class,
        TagKeyEntity::class,
        TagValueEntity::class,
        JamEntity::class,
        SetlistEntryEntity::class,
        SongHistoryEntryEntity::class,
        ComposerAliasEntity::class
    ],
    version = DatabaseVersions.ADDED_PLAY_COUNTS
)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun composerAliasDao(): ComposerAliasRoomDao
    abstract fun composerDao(): ComposerRoomDao
    abstract fun composerForSongDao(): ComposersForSongDao
    abstract fun dbStatisticsDao(): DbStatisticsRoomDao
    abstract fun gameAliasDao(): GameAliasRoomDao
    abstract fun gameDao(): GameRoomDao
    abstract fun jamDao(): JamRoomDao
    abstract fun setlistEntryDao(): SetlistEntryRoomDao
    abstract fun songDao(): SongRoomDao
    abstract fun songForComposerDao(): SongsForComposerDao
    abstract fun songForTagValueDao(): SongsForTagValueDao
    abstract fun songHistoryEntryDao(): SongHistoryEntryRoomDao
    abstract fun tagKeyDao(): TagKeyRoomDao
    abstract fun tagValueDao(): TagValueRoomDao
    abstract fun tagValueForSongDao(): TagValuesForSongDao

    abstract fun transactionDao(): TransactionDao
}
