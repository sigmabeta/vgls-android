package com.vgleadsheets.model.song

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.parts.Part

data class Song(
    val id: Long,
    val filename: String,
    val name: String,
    val composers: List<Composer>?,
    val parts: List<Part>?
) : ListItem<Song> {
    override fun isTheSameAs(theOther: Song?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Song?) = name == theOther?.name &&
            composers == theOther.composers &&
            parts == theOther.parts

    override fun getChangeType(theOther: Song?) = ListItem.CHANGE_ERROR
}
