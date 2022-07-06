package com.vgleadsheets.components

data class TitleListModel(
    val title: String,
    val subtitle: String,
    val onMenuButtonClick: (() -> Unit),
    val onImageLoadSuccess: (() -> Unit),
    val onImageLoadFail: ((Exception) -> Unit),
    val photoUrl: String? = null,
    val placeholder: Int? = R.drawable.ic_logo,
    val shouldSetMinHeightOnly: Boolean? = false,
    val shrinkPercent: Float = 0.0f,
    val hidden: Boolean = false,
) : ListModel {
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_title

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
