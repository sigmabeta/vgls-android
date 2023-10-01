package com.vgleadsheets.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    val placeholder: Int = com.vgleadsheets.ui_core.R.drawable.ic_logo,
    val shouldSetMinHeightOnly: Boolean? = true,
) : ListModel {
    @Composable
    override fun Content(modifier: Modifier) {
        Text(
            text = "Lol $title"
        )
    }

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
