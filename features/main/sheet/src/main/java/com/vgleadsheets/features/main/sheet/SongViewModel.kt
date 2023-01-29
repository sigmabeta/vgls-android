package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SongViewModel @AssistedInject constructor(
    @Assisted initialState: SongState,
    private val repository: VglsRepository,
) : MavericksViewModel<SongState>(initialState) {
    init {
        fetchSong()
        fetchAliases()
        fetchTagValues()
    }

    private fun fetchSong() = withState {
        repository.getSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        song = it
                    )
                )
            }
    }

    private fun fetchAliases() = withState {
        repository.getAliasesForSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songAliases = it
                    )
                )
            }
    }

    private fun fetchTagValues() = withState {
        repository.getTagValuesForSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        tagValues = it
                    )
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: SongState,
        ): SongViewModel
    }

    companion object : MavericksViewModelFactory<SongViewModel, SongState> {
        const val ID_COMPOSER_MULTIPLE = Long.MAX_VALUE

        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5

        override fun create(
            viewModelContext: ViewModelContext,
            state: SongState
        ): SongViewModel {
            val fragment: SongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
