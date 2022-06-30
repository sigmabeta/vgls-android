package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.storage.Setting

data class DebugContent(
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
