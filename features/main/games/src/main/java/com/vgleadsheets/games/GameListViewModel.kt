package com.vgleadsheets.games

import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class GameListViewModel(initialState: GameListState): MvRxViewModel<GameListState>(initialState) {
    fun fetchGames(repository: Repository) {
        repository.getGames()
            .execute { games ->
                copy(games = games)
            }
    }
}