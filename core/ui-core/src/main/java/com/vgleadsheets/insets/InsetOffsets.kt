package com.vgleadsheets.insets

data class InsetOffsets(
    val padding: CustomInset = CustomInset.DEFAULT,
    val margin: CustomInset = CustomInset.DEFAULT,
    val translation: CustomInset = CustomInset.DEFAULT
) {
    companion object {
        val DEFAULT = InsetOffsets()
    }
}
