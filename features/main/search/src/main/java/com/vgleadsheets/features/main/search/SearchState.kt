package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

data class SearchState(
    val query: String? = null,
    val songs: Async<List<Song>> = Uninitialized,
    val composers: Async<List<Composer>> = Uninitialized,
    val games: Async<List<Game>> = Uninitialized
) : MvRxState
