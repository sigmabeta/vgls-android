package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.storage.Storage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: VglsRepository,
    private val storage: Storage,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet
) : MavericksViewModel<ViewerState>(initialState) {

    private var screenLockTimer: Job? = null

    private var viewReportTimer: Job? = null

    private val _screenControlEvents = MutableSharedFlow<ScreenControlEvent>()
    val screenControlEvents = _screenControlEvents.asSharedFlow()

    init {
        checkScreenSetting()
    }

    private var hasViewBeenReported: Boolean = false

    fun fetchSong() = withState { state ->
        hasViewBeenReported = false
        val songId = state.songId

        if (songId != null) {
            repository.getSong(songId)
                .execute { data ->
                    stopReportTimer()
                    startReportTimer()

                    copy(
                        song = data,
                    )
                }
        }
    }

    fun checkScreenSetting() {
        suspend {
            storage.getSettingSheetScreenOn()
        }.execute {
            copy(keepScreenOnSetting = it)
        }
    }

    fun startReportTimer() = withState { state ->
        if (hasViewBeenReported) {
            return@withState
        }

        if (viewReportTimer?.isActive == true) {
            return@withState
        }

        val song = state.song() ?: return@withState

        viewReportTimer = viewModelScope.launch(dispatchers.computation) {
            hatchet.v("Starting view report timer for ${song.name}")
            delay(TIMER_VIEW_REPORT_MILLIS)

            hasViewBeenReported = true

            hatchet.v("Reporting song ${song.name} to db.")
            repository.incrementViewCounter(song.id)

            viewReportTimer = null
        }
    }

    fun stopReportTimer() {
        if (viewReportTimer != null) {
            hatchet.v("Stopping view report timer.")
            viewReportTimer?.cancel()
            viewReportTimer = null
        }
    }

    fun startScreenTimer() {
        screenLockTimer = viewModelScope.launch(dispatchers.computation) {
            hatchet.v("Starting screen timer.")
            _screenControlEvents.emit(ScreenControlEvent.TIMER_START)

            delay(TIMEOUT_SCREEN_OFF_MILLIS)

            hatchet.v("Screen timer expired.")
            _screenControlEvents.emit(ScreenControlEvent.TIMER_EXPIRED)
            screenLockTimer = null
        }
    }

    fun stopScreenTimer() {
        if (screenLockTimer != null) {
            hatchet.v("Clearing screen timer.")
            screenLockTimer?.cancel()
            screenLockTimer = null
        }
    }

    fun onNewJamSong(newSongId: Long) {
        setState {
            copy(songId = newSongId)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: ViewerState): ViewerViewModel
    }

    companion object : MavericksViewModelFactory<ViewerViewModel, ViewerState> {
        private const val TIMER_VIEW_REPORT_MILLIS = 10_000L

        private const val TIMEOUT_SCREEN_OFF_MINUTES = 10L
        const val TIMEOUT_SCREEN_OFF_MILLIS = TIMEOUT_SCREEN_OFF_MINUTES * 60 * 1_000L

        override fun create(
            viewModelContext: ViewModelContext,
            state: ViewerState
        ): ViewerViewModel {
            val fragment: ViewerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewerViewModelFactory.create(state)
        }
    }
}
