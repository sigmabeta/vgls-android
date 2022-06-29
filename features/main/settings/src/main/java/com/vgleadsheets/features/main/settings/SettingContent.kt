package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.*
import com.vgleadsheets.storage.Setting

data class SettingContent(
    val settings: Async<List<Setting>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = settings.failure()

    override fun isLoading() = settings.isLoading()

    override fun hasFailed() = settings.hasFailed()

    override fun isFullyLoaded() = settings.isReady()

    override fun isReady() = settings.isReady()

    override fun isEmpty() = settings()?.isEmpty() == true
}
