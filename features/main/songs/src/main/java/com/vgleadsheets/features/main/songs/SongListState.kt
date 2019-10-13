package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.song.Song

data class SongListState(
    val songs: Async<List<Song>> = Uninitialized
) : MvRxState
