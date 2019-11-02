package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.storage.Setting

data class ViewerState(
    val songId: Long,
    val song: Async<Song> = Uninitialized,
    val parts: Async<List<Part>> = Uninitialized,
    val screenOn: Async<Setting> = Uninitialized
) : MvRxState {
    constructor(songArgs: SongArgs) : this(songArgs.songId)
}
