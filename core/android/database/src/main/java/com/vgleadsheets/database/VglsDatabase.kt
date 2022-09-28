package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.ComposerAliasDao
import com.vgleadsheets.database.dao.ComposerDao
import com.vgleadsheets.database.dao.DbStatisticsDao
import com.vgleadsheets.database.dao.GameAliasDao
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.database.dao.JamDao
import com.vgleadsheets.database.dao.SetlistEntryDao
import com.vgleadsheets.database.dao.SongComposerDao
import com.vgleadsheets.database.dao.SongDao
import com.vgleadsheets.database.dao.SongHistoryEntryDao
import com.vgleadsheets.database.dao.SongTagValueDao
import com.vgleadsheets.database.dao.TagKeyDao
import com.vgleadsheets.database.dao.TagValueDao
import com.vgleadsheets.database.model.ComposerAliasEntity
import com.vgleadsheets.database.model.ComposerEntity
import com.vgleadsheets.database.model.GameAliasEntity
import com.vgleadsheets.database.model.GameEntity
import com.vgleadsheets.database.model.JamEntity
import com.vgleadsheets.database.model.SetlistEntryEntity
import com.vgleadsheets.database.model.SongEntity
import com.vgleadsheets.database.model.SongHistoryEntryEntity
import com.vgleadsheets.database.model.TagKeyEntity
import com.vgleadsheets.database.model.TagValueEntity
import com.vgleadsheets.database.model.TimeEntity
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
