package com.vgleadsheets.list

sealed class ColumnType {
    abstract fun numberOfColumns(width: WidthClass): Int

    data object One : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = 1
    }

    data class Regular(val widthInDp: Int) : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = width.averageWidthInDp / widthInDp
    }

    data class Staggered(val widthInDp: Int) : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = width.averageWidthInDp / widthInDp
    }
}
