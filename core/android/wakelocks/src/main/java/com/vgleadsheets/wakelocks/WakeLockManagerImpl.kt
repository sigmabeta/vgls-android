package com.vgleadsheets.wakelocks

import android.app.Activity
import android.view.WindowManager
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class WakeLockManagerImpl(
    private val activity: Activity,
    private val eventDispatcher: EventDispatcher,
    private val stringProvider: StringProvider,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) : WakeLockManager,
    ActionSink {
    private var screenOnTimerJob: Job? = null

    override fun keepScreenOn() {
        startTimer()
    }

    override fun allowScreenOff() {
        eventDispatcher.sendEvent(
            VglsEvent.ScreenOnTimerEnded(
                reason = "Disabled"
            )
        )
        endTimer()
    }

    override fun sendAction(action: VglsAction) {
        if (action == VglsAction.KeepScreenOnSnackCtaClicked) {
            startTimer()
        }
    }

    private fun startTimer() {
        screenOnTimerJob?.cancel()
        screenOnTimerJob = coroutineScope.launch(dispatchers.computation) {
            eventDispatcher.sendEvent(VglsEvent.ScreenOnTimerStarted)
            timerImpl()
        }
    }

    private fun endTimer() {
        coroutineScope.launch(dispatchers.main) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            screenOnTimerJob?.cancel()
        }
    }

    private suspend fun timerImpl() {
        withContext(dispatchers.main) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        delay(DURATION_SCREEN_ON)

        eventDispatcher.sendEvent(
            VglsEvent.ScreenOnTimerEnded(
                reason = "Expired"
            )
        )

        showScreenOffSnackbar()
        endTimer()
    }

    private fun showScreenOffSnackbar() {
        val actionDetails = VglsEvent.ShowSnackbar.SnackbarActionDetails(
            actionSink = this@WakeLockManagerImpl,
            clickAction = VglsAction.KeepScreenOnSnackCtaClicked,
            clickActionLabel = stringProvider.getString(StringId.SNACKBAR_CTA_SCREEN_OFF)
        )

        eventDispatcher.sendEvent(
            VglsEvent.ShowSnackbar(
                message = stringProvider.getString(StringId.SNACKBAR_SCREEN_OFF),
                withDismissAction = false,
                actionDetails = actionDetails,
                "WakeLockManagerImpl"
            )
        )
    }
    companion object {
        val DURATION_SCREEN_ON = 10.toDuration(DurationUnit.MINUTES)
    }
}
