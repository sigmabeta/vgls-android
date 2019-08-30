package com.vgleadsheets.model.song

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.composer.Composer

data class Song(
    val id: Long,
    val filename: String,
    val name: String,
    val pageCount: Int,
    val composers: List<Composer>
) : ListItem<Song> {
    override fun isTheSameAs(theOther: Song?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Song?) = name == theOther?.name &&
            pageCount == theOther.pageCount &&
            composers.size == theOther.composers.size

    override fun getChangeType(theOther: Song?) = ListItem.CHANGE_ERROR
}
