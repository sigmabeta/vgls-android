package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.ComposerDao
import com.vgleadsheets.database.dao.DbStatisticsDao
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.database.dao.PartDao
import com.vgleadsheets.database.dao.SongComposerDao
import com.vgleadsheets.database.dao.SongDao
import com.vgleadsheets.model.DbStatisticsEntity
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.SongEntity

@Database(
    entities = [GameEntity::class,
        SongEntity::class,
        ComposerEntity::class,
        SongComposerJoin::class,
        PartEntity::class,
        DbStatisticsEntity::class],
    version = 1
)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun songDao(): SongDao
    abstract fun partDao(): PartDao
    abstract fun composerDao(): ComposerDao
    abstract fun songComposerDao(): SongComposerDao
    abstract fun dbStatisticsDao(): DbStatisticsDao
}
