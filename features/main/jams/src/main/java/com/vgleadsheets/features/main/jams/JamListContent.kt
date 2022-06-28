package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.jam.Jam

data class JamListContent(
    val jamsLoad: Async<List<Jam>> = Uninitialized
) : SimpleListContent<Jam>(jamsLoad)
