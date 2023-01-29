package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.model.Song
import com.vgleadsheets.storage.BooleanSetting

data class ViewerState(
    val jamId: Long?,
    val songId: Long?,
    val song: Async<Song> = Uninitialized,
    val screenOn: Async<BooleanSetting> = Uninitialized,
    val activeJamId: Long? = null,
    val activeJamSheetId: Long? = null,
    val jamCancellationReason: String? = null,
    val hasViewBeenReported: Boolean = false,
) : MavericksState {
    constructor(viewerArgs: ViewerArgs) : this(viewerArgs.jamId, viewerArgs.songId)
}
