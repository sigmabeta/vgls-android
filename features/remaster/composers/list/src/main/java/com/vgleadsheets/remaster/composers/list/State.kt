package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.model.Composer
import com.vgleadsheets.state.VglsState

data class State(
    val composers: List<Composer> = emptyList()
) : VglsState
