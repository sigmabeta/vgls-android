package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.DbStatisticsDao
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.database.dao.SongDao
import com.vgleadsheets.model.DbStatisticsEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.song.SongEntity

@Database(
    entities = [GameEntity::class,
        SongEntity::class,
        DbStatisticsEntity::class],
    version = 1
)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun songDao(): SongDao
    abstract fun dbStatisticsDao(): DbStatisticsDao
}