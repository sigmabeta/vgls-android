package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song

data class ComposerData(
    val composer: Async<Composer> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(
            composer is Success &&
                    songs is Success &&
                    songs()?.isNotEmpty() == true
            )

    override fun canShowPartialData() = true
}
