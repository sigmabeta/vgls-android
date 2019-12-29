package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.storage.Setting

data class ViewerState(
    val songId: Long?,
    val song: Async<Song> = Uninitialized,
    val screenOn: Async<Setting> = Uninitialized,
    val activeJamId: Long? = null,
    val activeJamSheetId: Long? = null,
    val jamCancellationReason: String? = null
) : MvRxState {
    constructor(songArgs: SongArgs) : this(songArgs.songId)
}
