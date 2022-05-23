package com.vgleadsheets.features.main.list.sections

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
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
        photoUrl: String? = null,
        placeholder: Int? = R.drawable.ic_logo,
        titleGenerator: (() -> List<ListModel>)? = null
    ) = if (titleGenerator != null) {
        titleGenerator()
    } else {
        if (photoUrl == null) {
            onImageLoadSuccess?.invoke()
        }

        listOf(
            TitleListModel(
                title ?: resources.getString(R.string.app_name),
                subtitle ?: "",
                onImageLoadSuccess ?: Common.noop(),
                onImageLoadFail ?: Common.noopError(),
                photoUrl,
                placeholder
            )
        )
    }

    data class Config(
        val title: String?,
        val subtitle: String?,
        val resources: Resources,
        val onImageLoadSuccess: (() -> Unit)?,
        val onImageLoadFail: ((Exception) -> Unit)?,
        val photoUrl: String? = null,
        val placeholder: Int? = R.drawable.ic_logo,
        val titleGenerator: (() -> List<ListModel>)? = null
    )
}
