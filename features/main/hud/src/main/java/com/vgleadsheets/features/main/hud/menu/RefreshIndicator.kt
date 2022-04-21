package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuLoadingItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.PerfTracker

object RefreshIndicator {
    fun getListModels(
        refreshing: Boolean,
        resources: Resources,
        perfTracker: PerfTracker
    ) = if (refreshing) {
        getRefreshIndicatorListModels(resources, perfTracker)
    } else {
        emptyList()
    }

    private fun getRefreshIndicatorListModels(
        resources: Resources,
        perfTracker: PerfTracker
    ) = listOf(
        getRefreshIndicatorListModel(resources, perfTracker)
    )

    private fun getRefreshIndicatorListModel(
        resources: Resources,
        perfTracker: PerfTracker
    ) = MenuLoadingItemListModel(
        resources.getString(R.string.label_refresh_loading),
        R.drawable.ic_refresh_24dp,
        "",
        perfTracker
    )
}