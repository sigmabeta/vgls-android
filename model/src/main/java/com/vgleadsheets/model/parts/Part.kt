package com.vgleadsheets.model.parts

import com.vgleadsheets.model.ListItem

data class Part(val id: Long, val part: String) : ListItem<Part> {
    override fun isTheSameAs(theOther: Part?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Part?) = part == theOther?.part

    override fun getChangeType(theOther: Part?) = ListItem.CHANGE_ERROR
}