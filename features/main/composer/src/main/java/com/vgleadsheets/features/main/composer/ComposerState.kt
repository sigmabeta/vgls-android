package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song

data class ComposerState(
    val composerId: Long,
    val composer: Async<Composer> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}
