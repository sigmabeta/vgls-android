package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry

data class JamData(
    val jam: Async<Jam> = Uninitialized,
    val jamRefresh: Async<ApiJam> = Uninitialized,
    val setlist: Async<List<SetlistEntry>> = Uninitialized,
    val setlistRefresh: Async<List<Long>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(
            jam is Success &&
                    jam()?.currentSong != null
            )

    override fun isLoading() = jam is Loading

    override fun canShowPartialData() = true
}
