package com.vgleadsheets.model.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.song.Song

@Entity
data class Game(
    val name: String
) : ListItem<Game> {
    @PrimaryKey(autoGenerate = true) var id: Long? = null

    override fun isTheSameAs(theOther: Game?) = name == theOther?.name

    override fun hasSameContentAs(theOther: Game?) = name == theOther?.name /*&& songs.size == theOther.songs.size*/

    override fun getChangeType(theOther: Game?) = ListItem.CHANGE_ERROR
}