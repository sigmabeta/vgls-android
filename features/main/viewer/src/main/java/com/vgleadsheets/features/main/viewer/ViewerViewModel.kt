package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: Repository,
    private val storage: Storage
) : MvRxViewModel<ViewerState>(initialState) {

    private var jamDisposables = CompositeDisposable()

    init {
        fetchSong()
        fetchParts()
        checkScreenSetting()
    }

    fun checkScreenSetting() {
        storage.getSettingSheetScreenOn()
            .execute {
                copy(screenOn = it)
            }
    }

    fun followJam(jamId: Long) {
        Timber.i("Following jam.")
        repository.getJam(jamId)
            .firstOrError()
            .subscribe(
                {
                    subscribeToJamNetwork(it)
                },
                {
                    val message = "Failed to get Jam from database: ${it.message}"
                    Timber.e(message)
                    setState { copy(jamCancellationReason = message) }
                }
            )
            .disposeOnClear()

        subscribeToJamDatabase(jamId)
    }

    fun unfollowJam() {
        Timber.i("Following jam.")
        jamDisposables.clear()
    }

    private fun subscribeToJamDatabase(jamId: Long) {
        Timber.i("Subscribing to jam $jamId in the database.")
        val databaseRefresh = repository.observeJamState(jamId)
            .subscribe(
                {
                    setState { copy(activeJamSheetId = it.currentSong.id) }
                },
                {
                    val message = "Error observing Jam: ${it.message}"
                    Timber.e(message)
                    setState { copy(jamCancellationReason = message) }
                }
            )
            .disposeOnClear()

        jamDisposables.add(databaseRefresh)
    }

    private fun subscribeToJamNetwork(it: Jam) {
        Timber.i("Subscribing to jam ${it.id} on the network.")
        val networkRefresh = repository.refreshJamStateContinuously(it.name)
            .subscribe({},
                {
                    val message = "Error refreshing Jam: ${it.message}"
                    Timber.e(message)
                    setState { copy(jamCancellationReason = message) }
                }
            )
            .disposeOnClear()

        jamDisposables.add(networkRefresh)
    }

    private fun fetchSong() = withState { state ->
        repository.getSong(state.songId)
            .execute { data ->
                copy(song = data)
            }
    }

    private fun fetchParts() = withState { state ->
        repository.getPartsForSong(state.songId)
            .execute {
                copy(parts = it)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ViewerState): ViewerViewModel
    }

    companion object : MvRxViewModelFactory<ViewerViewModel, ViewerState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ViewerState
        ): ViewerViewModel? {
            val fragment: ViewerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewerViewModelFactory.create(state)
        }
    }
}
