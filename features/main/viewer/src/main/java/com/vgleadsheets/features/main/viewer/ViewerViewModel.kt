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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.UnknownHostException

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: Repository,
    private val storage: Storage
) : MvRxViewModel<ViewerState>(initialState) {

    private var jamDisposables = CompositeDisposable()

    init {
        checkScreenSetting()
    }

    fun fetchSong() = withState { state ->
        val songId = state.songId

        if (songId != null) {
            Timber.i("Fetching song.")
            repository.getSong(songId)
                .execute { data ->
                    copy(song = data)
                }
        }
    }

    fun updateSongId(newSheetId: Long) { setState { copy(songId = newSheetId) } }

    fun checkScreenSetting() {
        storage.getSettingSheetScreenOn()
            .execute {
                copy(screenOn = it)
            }
    }

    fun clearCancellationReason() = setState { copy(jamCancellationReason = null) }

    fun unfollowJam(reason: String?) {
        if (jamDisposables.isDisposed) {
            return
        }

        jamDisposables.clear()

        if (reason != null) {
            setState { copy(jamCancellationReason = reason) }
        }
    }

    fun followJam() = withState { state ->
        val jamId = state.jamId

        if (jamId != null) {
            Timber.i("Following jam.")
            val jamStateObservation = repository.getJam(jamId, false)
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

            jamDisposables.add(jamStateObservation)

            subscribeToJamDatabase(jamId)
        }
    }

    fun cancelPerf() {
        TODO("Not yet implemented")
    }

    private fun subscribeToJamDatabase(jamId: Long) {
        Timber.i("Subscribing to jam $jamId in the database.")
        val databaseRefresh = repository.observeJamState(jamId)
            .subscribe(
                {
                    // TODO Do more than a null check here; should we report an error if the *song* is null?
                    setState { copy(activeJamSheetId = it.currentSong?.id) }
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

    private fun subscribeToJamNetwork(jam: Jam) {
        Timber.i("Subscribing to jam ${jam.id} on the network.")
        val networkRefresh = repository.refreshJamStateContinuously(jam.name)
            .subscribe({},
                {
                    val message: String
                    if (it is HttpException) {
                        if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                            message = "Jam has been deleted from server."
                            removeJam(jam.id)
                        } else {
                            message = "Error communicating with Jam server."
                        }
                    } else if (it is UnknownHostException) {
                        message = "Can't reach Jam server. Check connection and try again."
                    } else {
                        message = "Error communicating with Jam server."
                    }

                    Timber.e(message)
                    setState { copy(jamCancellationReason = message) }
                }
            )
            .disposeOnClear()

        jamDisposables.add(networkRefresh)
    }

    private fun removeJam(dataId: Long) {
        repository
            .removeJam(dataId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { Timber.e("Error removing Jam: ${it.message}") })
            .disposeOnClear()
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
