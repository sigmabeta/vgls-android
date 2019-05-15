package com.vgleadsheets.model.game

import androidx.room.Entity
import com.vgleadsheets.model.song.Song

@Entity
data class Game(val name: String,
                val songs: List<Song>)