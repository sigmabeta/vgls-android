package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuLoadingItemListModel
import com.vgleadsheets.features.main.hud.R

object RefreshIndicator {
    fun getListModels(
        refreshing: Boolean,
        resources: Resources
    ) = if (refreshing) {
        getRefreshIndicatorListModels(resources)
    } else {
        emptyList()
    }

    private fun getRefreshIndicatorListModels(
        resources: Resources
    ) = listOf(
        getRefreshIndicatorListModel(resources)
    )

    private fun getRefreshIndicatorListModel(
        resources: Resources,
    ) = MenuLoadingItemListModel(
        resources.getString(R.string.label_refresh_loading),
        R.drawable.ic_refresh_24dp,
    )
}
