package com.vgleadsheets.model.game

import android.app.LauncherActivity
import androidx.room.Entity
import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.song.Song

@Entity
data class Game(val name: String,
                val songs: List<Song>) : ListItem<Game> {
    override fun isTheSameAs(theOther: Game?) = name == theOther?.name

    override fun hasSameContentAs(theOther: Game?) = name == theOther?.name && songs.size == theOther.songs.size

    override fun getChangeType(theOther: Game?) = ListItem.CHANGE_ERROR
}