package com.vgleadsheets.features.main.list.sections

import android.content.res.Resources
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.Common
import com.vgleadsheets.features.main.list.R

object Title {
    fun model(
        title: String?,
        subtitle: String?,
        shouldShowBack: Boolean,
        onImageLoadSuccess: (() -> Unit)?,
        onImageLoadFail: ((Exception) -> Unit)?,
        resources: Resources,
        onMenuButtonClick: (() -> Unit) = Common.noop(),
        photoUrl: String? = null,
        placeholder: Int? = com.vgleadsheets.ui_core.R.drawable.ic_logo,
        allowExpansion: Boolean = true,
        isLoading: Boolean = false,
        titleGenerator: (() -> TitleListModel)? = null
    ) = if (titleGenerator != null) {
        titleGenerator()
    } else {
        if (photoUrl == null) {
            onImageLoadSuccess?.invoke()
        }

        TitleListModel(
            title ?: resources.getString(com.vgleadsheets.ui_core.R.string.app_name),
            if (isLoading) {
                resources.getString(R.string.loading)
            } else {
                subtitle ?: ""
            },
            isLoading,
            shouldShowBack,
            !allowExpansion,
            onMenuButtonClick,
            onImageLoadSuccess ?: Common.noop(),
            onImageLoadFail ?: Common.noopError(),
            photoUrl,
            placeholder
        )
    }

    data class Config(
        val title: String?,
        val subtitle: String?,
        val resources: Resources,
        val onImageLoadSuccess: (() -> Unit)?,
        val onImageLoadFail: ((Exception) -> Unit)?,
        val photoUrl: String? = null,
        val placeholder: Int? = com.vgleadsheets.ui_core.R.drawable.ic_logo,
        val allowExpansion: Boolean = true,
        val isLoading: Boolean = false,
        val titleGenerator: (() -> TitleListModel)? = null,
        val onMenuButtonClick: (() -> Unit) = Common.noop()
    )
}
