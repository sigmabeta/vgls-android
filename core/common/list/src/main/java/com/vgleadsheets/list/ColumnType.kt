package com.vgleadsheets.list

sealed class ColumnType {
    abstract fun numberOfColumns(width: WidthClass): Int

    data object One : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = 1
    }

    data class Fixed(val numberOfColumns: Int) : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = numberOfColumns
    }

    data class Adaptive(val sizeInDp: Int) : ColumnType() {
        override fun numberOfColumns(width: WidthClass) = width.averageWidthInDp / sizeInDp
    }
}
