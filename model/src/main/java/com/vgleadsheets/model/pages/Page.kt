package com.vgleadsheets.model.pages

import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.ListItem.Companion.CHANGE_ERROR

data class Page(val number: Int, val imageUrl: String) : ListItem<Page> {
    override fun isTheSameAs(theOther: Page?) = number == theOther?.number

    override fun hasSameContentAs(theOther: Page?) = imageUrl == theOther?.imageUrl

    override fun getChangeType(theOther: Page?) = CHANGE_ERROR
}
