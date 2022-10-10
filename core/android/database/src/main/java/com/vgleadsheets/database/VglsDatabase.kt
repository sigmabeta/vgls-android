package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.*
import com.vgleadsheets.database.enitity.*
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.joins.SongTagValueJoin

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
    version = 9
)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun songDao(): SongDao
    abstract fun composerDao(): ComposerDao
    abstract fun gameAliasDao(): GameAliasDao
    abstract fun tagKeyDao(): TagKeyDao
    abstract fun tagValueDao(): TagValueDao
    abstract fun composerAliasDao(): ComposerAliasDao
    abstract fun songComposerDao(): SongComposerDao
    abstract fun songTagValueDao(): SongTagValueDao
    abstract fun jamDao(): JamDao
    abstract fun setlistEntryDao(): SetlistEntryDao
    abstract fun songHistoryEntryDao(): SongHistoryEntryDao
    abstract fun dbStatisticsDao(): DbStatisticsDao
}
