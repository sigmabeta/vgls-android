package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import timber.log.Timber

class JamViewModel @AssistedInject constructor(
    @Assisted initialState: JamState,
    private val repository: Repository
) : MvRxViewModel<JamState>(initialState) {

    init {
        fetchJam()
    }

    fun refreshJam(jamId: Long, jamName: String) {
        Timber.i("Refreshing jam...")

        repository
            .refreshJamState(jamName)
            .execute {
                copy(jamRefresh = it)
            }

        repository
            .refreshSetlist(jamId, jamName)
            .execute {
                copy(setlistRefresh = it)
            }
    }

    fun deleteJam() = withState {
        repository.removeJam(it.jamId)
            .execute {
                copy(deletion = it)
            }
    }

    private fun fetchJam() = withState { state ->
        val jamId = state.jamId

        repository.getJam(jamId, true)
            .execute { newJam ->
                copy(jam = newJam)
            }

        repository.getSetlistForJam(jamId)
            .execute { newSetlist ->
                copy(setlist = newSetlist)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: JamState): JamViewModel
    }

    companion object : MvRxViewModelFactory<JamViewModel, JamState> {
        override fun create(viewModelContext: ViewModelContext, state: JamState): JamViewModel? {
            val fragment: JamFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.jamViewModelFactory.create(state)
        }
    }
}
