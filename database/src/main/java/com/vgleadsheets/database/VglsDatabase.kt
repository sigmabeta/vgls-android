package com.vgleadsheets.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.database.dao.SongDao
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

@Database(entities = [Game::class, Song::class], version = 1)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun songDao(): SongDao
}