package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

data class GameData(
    val game: Async<Game> = Uninitialized,
    val songs: Async<List<Song>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(
            game is Success &&
                    songs is Success &&
                    songs()?.isNotEmpty() == true
            )

    override fun isLoading() = songs is Loading

    override fun canShowPartialData() = game is Success
}
