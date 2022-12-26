package com.vgleadsheets.features.main.favorites

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FavoriteListViewModel @AssistedInject constructor(
    @Assisted initialState: FavoriteListState,
    private val repository: VglsRepository,
) : MavericksViewModel<FavoriteListState>(initialState) {
    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        repository.getFavoriteGames()
            .execute { newGames ->
                copy(
                    contentLoad = contentLoad.copy(
                        gamesLoad = newGames
                    )
                )
            }

        repository.getFavoriteSongs()
            .execute { newSongs ->
                copy(
                    contentLoad = contentLoad.copy(
                        songsLoad = newSongs
                    )
                )
            }

        repository.getFavoriteComposers()
            .execute { newComposers ->
                copy(
                    contentLoad = contentLoad.copy(
                        composerLoad = newComposers
                    )
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: FavoriteListState
        ): FavoriteListViewModel
    }

    companion object : MavericksViewModelFactory<FavoriteListViewModel, FavoriteListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: FavoriteListState
        ): FavoriteListViewModel {
            val fragment: FavoriteListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
