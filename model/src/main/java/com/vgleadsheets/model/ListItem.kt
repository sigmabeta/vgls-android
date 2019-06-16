package com.vgleadsheets.model

interface ListItem<T : ListItem<T>> {
    fun isTheSameAs(theOther: T?): Boolean

    fun hasSameContentAs(theOther: T?): Boolean

    fun getChangeType(theOther: T?): Int

    companion object {
        val CHANGE_ERROR = -1
    }
}