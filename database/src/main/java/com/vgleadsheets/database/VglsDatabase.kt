package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.ComposerAliasDao
import com.vgleadsheets.database.dao.ComposerDao
import com.vgleadsheets.database.dao.DbStatisticsDao
import com.vgleadsheets.database.dao.GameAliasDao
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.database.dao.JamDao
import com.vgleadsheets.database.dao.PageDao
import com.vgleadsheets.database.dao.PartDao
import com.vgleadsheets.database.dao.SetlistEntryDao
import com.vgleadsheets.database.dao.SongComposerDao
import com.vgleadsheets.database.dao.SongDao
import com.vgleadsheets.database.dao.SongHistoryEntryDao
import com.vgleadsheets.database.dao.SongTagValueDao
import com.vgleadsheets.database.dao.TagKeyDao
import com.vgleadsheets.database.dao.TagValueDao
import com.vgleadsheets.model.alias.ComposerAliasEntity
import com.vgleadsheets.model.alias.GameAliasEntity
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.jam.JamEntity
import com.vgleadsheets.model.jam.SetlistEntryEntity
import com.vgleadsheets.model.jam.SongHistoryEntryEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.tag.TagKeyEntity
import com.vgleadsheets.model.tag.TagValueEntity
import com.vgleadsheets.model.time.TimeEntity

@Database(
    entities = [GameEntity::class,
        SongEntity::class,
        ComposerEntity::class,
        SongComposerJoin::class,
        SongTagValueJoin::class,
        PartEntity::class,
        PageEntity::class,
        TimeEntity::class,
        GameAliasEntity::class,
        TagKeyEntity::class,
        TagValueEntity::class,
        JamEntity::class,
        SetlistEntryEntity::class,
        SongHistoryEntryEntity::class,
        ComposerAliasEntity::class],
    version = 5
)
@Suppress("TooManyFunctions")
abstract class VglsDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun songDao(): SongDao
    abstract fun partDao(): PartDao
    abstract fun pageDao(): PageDao
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
