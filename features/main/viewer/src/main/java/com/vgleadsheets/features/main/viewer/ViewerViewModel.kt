package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.time.Duration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import retrofit2.HttpException
import timber.log.Timber

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: Repository,
    private val storage: Storage,
    private val dispatchers: VglsDispatchers
) : MavericksViewModel<ViewerState>(initialState) {
    // Card shark
    private var jamJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    private var timer = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    private val _screenControlEvents = MutableSharedFlow<ScreenControlEvent>()
    val screenControlEvents = _screenControlEvents.asSharedFlow()

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

    fun updateSongId(newSheetId: Long) {
        setState { copy(songId = newSheetId) }
    }

    fun checkScreenSetting() {
        suspend {
            storage.getSettingSheetScreenOn()
        }.execute {
            copy(screenOn = it)
        }
    }

    fun clearCancellationReason() = setState { copy(jamCancellationReason = null) }

    fun unfollowJam(reason: String?) {
        if (jamJob.isCancelled) {
            return
        }

        jamJob.cancel()

        if (reason != null) {
            setState { copy(jamCancellationReason = reason) }
        }
    }

    fun followJam() = withState { state ->
        val jamId = state.jamId

        if (jamId != null) {
            // I'm pretty sure what this means is that cancelling jamJob will cancel this job.
            viewModelScope.launch(dispatchers.disk + jamJob) {
                Timber.i("Following jam.")
                repository.getJam(jamId, false)
                    .catch {
                        val message = "Failed to get Jam from database: ${it.message}"
                        Timber.e(message)
                        setState { copy(jamCancellationReason = message) }
                    }
                    .onEach { subscribeToJamNetwork(it) }
                    .firstOrNull()
            }

            subscribeToJamDatabase(jamId)
        }
    }

    fun startScreenTimer() {
        viewModelScope.launch(dispatchers.computation + timer) {
            Timber.v("Starting screen timer.")
            _screenControlEvents.emit(ScreenControlEvent.TIMER_START)

            val minutes = Duration.ofMinutes(TIMEOUT_SCREEN_OFF_MINUTES)
            delay(minutes.toMillis())

            Timber.v("Screen timer expired.")
            _screenControlEvents.emit(ScreenControlEvent.TIMER_EXPIRED)
        }
    }

    fun stopScreenTimer() {
        Timber.v("Clearing screen timer.")
        timer.cancel()
    }

    private fun subscribeToJamDatabase(jamId: Long) {
        Timber.i("Subscribing to jam $jamId in the database.")
        repository.observeJamState(jamId)
            .onEach {
                setState {
                    // TODO Do more than a null check here; should we report an error if the *song* is null?
                    copy(activeJamSheetId = it.currentSong?.id)
                }
            }.catch {
                val message = "Error observing Jam: ${it.message}"
                Timber.e(message)
                setState { copy(jamCancellationReason = message) }
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope + jamJob)
    }

    private fun subscribeToJamNetwork(jam: Jam) {
        Timber.i("Subscribing to jam ${jam.id} on the network.")
        repository.refreshJamStateContinuously(jam.name)
            .onEach { }
            .catch {
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
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope + jamJob)
    }

    private fun removeJam(dataId: Long) {
        viewModelScope.launch(dispatchers.disk) {
            try {
                repository.removeJam(dataId)
            } catch (ex: Exception) {
                Timber.e("Error removing Jam: ${ex.message}")
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ViewerState): ViewerViewModel
    }

    companion object : MavericksViewModelFactory<ViewerViewModel, ViewerState> {
        const val TIMEOUT_SCREEN_OFF_MINUTES = 10L

        override fun create(
            viewModelContext: ViewModelContext,
            state: ViewerState
        ): ViewerViewModel? {
            val fragment: ViewerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewerViewModelFactory.create(state)
        }
    }
}
