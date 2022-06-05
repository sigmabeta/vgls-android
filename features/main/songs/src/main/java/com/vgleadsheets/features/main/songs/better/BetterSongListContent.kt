package com.vgleadsheets.features.main.songs.better

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.SimpleListContent
import com.vgleadsheets.model.song.Song

data class BetterSongListContent(
    val songsLoad: Async<List<Song>> = Uninitialized
) : SimpleListContent<Song>(songsLoad)
