package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

class JamViewModel @AssistedInject constructor(
    @Assisted initialState: JamState,
    private val repository: VglsRepository,
) : MavericksViewModel<JamState>(initialState) {
    init {
        fetchJam()
    }

    private var firstRefresh = true

    fun refreshJam() = withState { state ->
        Timber.i("Refreshing jam...")

        val name = state.contentLoad.jam()?.name ?: return@withState
        fireJamRefreshRequest(name, state)
    }

    fun deleteJam() = withState {
        suspend {
            repository.removeJam(it.jamId)
        }.execute {
            copy(
                deletion = it
            )
        }
    }

    private fun fireJamRefreshRequest(
        name: String,
        state: JamState
    ) {
        suspend {
            repository.refreshJamState(name)
        }.execute {
            copy(
                contentLoad = contentLoad.copy(
                    jamRefresh = it
                )
            )
        }

        suspend {
            repository.refreshSetlist(state.jamId, name)
        }.execute {
            copy(
                contentLoad = contentLoad.copy(
                    setlistRefresh = it
                )
            )
        }
    }

    private fun fetchJam() = withState { state ->
        val jamId = state.jamId

        repository.getJam(jamId, true)
            .execute {
                val jamName = it.content()?.name
                if (jamName != null && firstRefresh) {
                    fireJamRefreshRequest(jamName, this)
                    firstRefresh = false
                }

                copy(
                    contentLoad = contentLoad.copy(
                        jam = it
                    )
                )
            }

        // repository.getSetlistForJam(jamId)
        //     .execute {
        //         copy(
        //             contentLoad = contentLoad.copy(
        //                 setlist = it
        //             )
        //         )
        //     }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: JamState,
        ): JamViewModel
    }

    companion object : MavericksViewModelFactory<JamViewModel, JamState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: JamState
        ): JamViewModel {
            val fragment: JamFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
