package com.vgleadsheets.features.main.hud.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

data class SearchContent(
    val songs: Async<List<Song>> = Uninitialized,
    val composers: Async<List<Composer>> = Uninitialized,
    val games: Async<List<Game>> = Uninitialized,
    val searching: Boolean
)
