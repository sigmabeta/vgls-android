package com.vgleadsheets.components

import com.vgleadsheets.ui.Icon

data class TitleListModel(
    val title: String,
    val subtitle: String,
    val isLoading: Boolean,
    val shouldShowBack: Boolean,
    val allowExpansion: Boolean,
    val onMenuButtonClick: (() -> Unit),
    val onImageLoadSuccess: (() -> Unit),
    val onImageLoadFail: ((Exception) -> Unit),
    val photoUrl: String? = null,
    val placeholder: Icon,
    val shouldSetMinHeightOnly: Boolean? = true,
) : ListModel() {
    override val dataId = "listitem.title.$title".hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL

    override fun hashCode(): Int {
        return title.hashCode() + subtitle.hashCode() + photoUrl.hashCode() + placeholder.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is TitleListModel) {
            return hashCode() == other.hashCode()
        }
        return super.equals(other)
    }
}
