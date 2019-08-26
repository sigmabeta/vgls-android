package com.vgleadsheets.model.search

import com.vgleadsheets.model.ListItem

data class SearchResult(
    val id: Long,
    val type: SearchResultType,
    val name: String
) : ListItem<SearchResult> {
    override fun isTheSameAs(theOther: SearchResult?) = id == theOther?.id

    override fun hasSameContentAs(theOther: SearchResult?) = name == theOther?.name && type == theOther.type

    override fun getChangeType(theOther: SearchResult?) = ListItem.CHANGE_ERROR
}
