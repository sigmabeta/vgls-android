package com.vgleadsheets.components

sealed class ListModel {
    abstract val dataId: Long
    abstract val columns: Int

    fun layoutId() = this::javaClass.name.hashCode()

    companion object {
        const val COLUMNS_ALL = -1
    }
}
