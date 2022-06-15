package com.vgleadsheets.features.main.jam.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry

data class BetterJamContent(
    val jamRefresh: Async<ApiJam> = Uninitialized,
    val jam: Async<Jam> = Uninitialized,
    val setlistRefresh: Async<List<Long>> = Uninitialized,
    val setlist: Async<List<SetlistEntry>> = Uninitialized
) : ListContent {
    // TODO CompositeException
    override fun failure() = jam.failure() ?: setlist.failure()

    override fun isLoading() = setlist.isLoading()

    override fun hasFailed() = jam.hasFailed() || setlist.hasFailed()

    override fun isFullyLoaded() = jam.isReady() && setlist.isReady()

    override fun isReady() = jam.isReady()

    override fun isEmpty() = setlist()?.isEmpty() == true
}
