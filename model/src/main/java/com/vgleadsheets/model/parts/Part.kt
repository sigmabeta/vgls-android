package com.vgleadsheets.model.parts

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.pages.Page

data class Part(
    val id: Long,
    val name: String,
    val pages: List<Page>?
) : ListItem<Part> {
    override fun isTheSameAs(theOther: Part?) = id == theOther?.id

    override fun hasSameContentAs(theOther: Part?) = name == theOther?.name &&
            pages == theOther.pages

    override fun getChangeType(theOther: Part?) = ListItem.CHANGE_ERROR
}
