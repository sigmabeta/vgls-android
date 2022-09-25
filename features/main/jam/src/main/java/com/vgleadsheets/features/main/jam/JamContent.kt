package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry

data class JamContent(
    val jamRefresh: Async<Unit> = Uninitialized,
    val jam: Async<Jam> = Uninitialized,
    val setlistRefresh: Async<Unit> = Uninitialized,
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
