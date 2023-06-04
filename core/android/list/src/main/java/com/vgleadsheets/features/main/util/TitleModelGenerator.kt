package com.vgleadsheets.features.main.util

import android.content.res.Resources
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.sections.Title

object TitleModelGenerator {
    fun generateUsingConfig(
        titleConfig: Title.Config,
        alwaysShowBack: Boolean,
        resources: Resources,
        onAppBarButtonClick: () -> Unit
    ): TitleListModel {
        return Title.model(
            titleConfig.title,
            titleConfig.subtitle,
            alwaysShowBack,
            titleConfig.onImageLoadSuccess,
            titleConfig.onImageLoadFail,
            resources,
            onAppBarButtonClick,
            titleConfig.photoUrl,
            titleConfig.placeholder,
            titleConfig.allowExpansion,
            titleConfig.isLoading,
            titleConfig.titleGenerator,
        )
    }
}
