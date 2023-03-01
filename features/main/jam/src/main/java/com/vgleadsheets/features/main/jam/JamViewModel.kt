package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class JamViewModel @AssistedInject constructor(
    @Assisted initialState: JamState,
    private val repository: VglsRepository,
    private val hatchet: Hatchet,
) : MavericksViewModel<JamState>(initialState) {
    init {
        fetchJam()
    }

    private var firstRefresh = true

    fun refreshJam() = withState { state ->
        hatchet.i(this.javaClass.simpleName, "Refreshing jam...")

        val name = state.contentLoad.jam()?.name ?: return@withState
        fireJamRefreshRequest(name)
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

    private fun fireJamRefreshRequest(name: String) {
        suspend {
            repository.refreshJamState(name)
        }.execute {
            copy(
                contentLoad = contentLoad.copy(
                    jamRefresh = it
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
                    fireJamRefreshRequest(jamName)
                    firstRefresh = false
                }

                copy(
                    contentLoad = contentLoad.copy(
                        jam = it
                    )
                )
            }

        repository.getSetlistEntriesForJam(jamId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        setlist = it
                    )
                )
            }

        repository.getSongHistoryForJam(jamId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songHistory = it
                    )
                )
            }
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
