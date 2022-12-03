package com.vgleadsheets.insets

data class CustomInset(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
) {
    companion object {
        val DEFAULT = CustomInset()
    }
}
