package com.vgleadsheets.model.composer

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.song.Song

data class Composer(
    val id: Long?,
    val name: String,
    val songs: List<Song>?
) : ListItem<Composer> {
    override fun isTheSameAs(theOther: Composer?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Composer?) = name == theOther?.name
        && songs?.size == theOther.songs?.size

    override fun getChangeType(theOther: Composer?) = ListItem.CHANGE_ERROR
}
