package com.vgleadsheets.components

data class TitleListModel(
    val title: String,
    val subtitle: String,
    val isLoading: Boolean,
    val onMenuButtonClick: (() -> Unit),
    val onImageLoadSuccess: (() -> Unit),
    val onImageLoadFail: ((Exception) -> Unit),
    val photoUrl: String? = null,
    val placeholder: Int? = R.drawable.ic_logo,
    val shouldSetMinHeightOnly: Boolean? = true,
) : ListModel {
    override val dataId = R.string.cont_desc_logo.toLong()
    override val layoutId = R.string.cont_desc_logo

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
