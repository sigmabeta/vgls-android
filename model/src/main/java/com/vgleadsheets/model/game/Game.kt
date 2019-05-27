package com.vgleadsheets.model.game

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.song.Song

data class Game(
    val id: Long,
    val name: String,
    val songs: List<Song>
) : ListItem<Game> {
    override fun isTheSameAs(theOther: Game?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Game?) = name == theOther?.name && songs.size == theOther.songs.size

    override fun getChangeType(theOther: Game?) = ListItem.CHANGE_ERROR
}