package com.vgleadsheets.features.main.list.sections

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.Common
import com.vgleadsheets.features.main.list.R

object Title {
    fun listItems(
        title: String?,
        subtitle: String?,
        onImageLoadSuccess: (() -> Unit)?,
        onImageLoadFail: ((Exception) -> Unit)?,
        resources: Resources,
        onMenuButtonClick: (() -> Unit) = Common.noop(),
        photoUrl: String? = null,
        placeholder: Int? = R.drawable.ic_logo,
        shouldShow: Boolean = true,
        isLoading: Boolean = false,
        titleGenerator: (() -> List<ListModel>)? = null
    ) = if (titleGenerator != null) {
        titleGenerator()
    } else {
        if (photoUrl == null) {
            onImageLoadSuccess?.invoke()
        }

        if (!shouldShow) {
            emptyList()
        } else if (isLoading) {
            listOf(
                LoadingTitleListModel(
                    onMenuButtonClick = onMenuButtonClick
                )
            )
        } else {
            listOf(
                TitleListModel(
                    title ?: resources.getString(R.string.app_name),
                    subtitle ?: "",
                    onMenuButtonClick,
                    onImageLoadSuccess ?: Common.noop(),
                    onImageLoadFail ?: Common.noopError(),
                    photoUrl,
                    placeholder
                )
            )
        }
    }

    data class Config(
        val title: String?,
        val subtitle: String?,
        val resources: Resources,
        val onImageLoadSuccess: (() -> Unit)?,
        val onImageLoadFail: ((Exception) -> Unit)?,
        val photoUrl: String? = null,
        val placeholder: Int? = R.drawable.ic_logo,
        val shouldShow: Boolean = true,
        val isLoading: Boolean = false,
        val titleGenerator: (() -> List<ListModel>)? = null,
        val onMenuButtonClick: (() -> Unit) = Common.noop()
    )
}
