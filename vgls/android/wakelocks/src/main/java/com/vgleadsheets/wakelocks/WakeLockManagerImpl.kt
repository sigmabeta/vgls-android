package com.vgleadsheets.wakelocks

import android.app.Activity
import android.view.WindowManager
import com.vgleadsheets.strings.VglsStringId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.ui.StringProvider
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class WakeLockManagerImpl(
    private val activity: Activity,
    private val eventDispatcher: EventDispatcher,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: SageDispatchers,
    private val stringProvider: StringProvider,
) : WakeLockManager,
    ActionSink {
    private var screenOnTimerJob: Job? = null

    override fun keepScreenOn() {
        startTimer()
    }

    override fun allowScreenOff() {
        eventDispatcher.sendEvent(
            SageEvent.ScreenOnTimerEnded(
                reason = "Disabled"
            )
        )
        endTimer()
    }

    override fun sendAction(action: SageAction) {
        if (action == SageAction.KeepScreenOnSnackCtaClicked) {
            startTimer()
        }
    }

    private fun startTimer() {
        screenOnTimerJob?.cancel()
        screenOnTimerJob = coroutineScope.launch(dispatchers.computation) {
            eventDispatcher.sendEvent(SageEvent.ScreenOnTimerStarted)
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
            SageEvent.ScreenOnTimerEnded(
                reason = "Expired"
            )
        )

        showScreenOffSnackbar()
        endTimer()
    }

    private fun showScreenOffSnackbar() {
        val actionDetails = SageEvent.ShowSnackbar.SnackbarActionDetails(
            actionSink = this@WakeLockManagerImpl,
            clickAction = SageAction.KeepScreenOnSnackCtaClicked,
            clickActionLabel = stringProvider.getString(VglsStringId.SNACKBAR_CTA_SCREEN_OFF)
        )

        eventDispatcher.sendEvent(
            SageEvent.ShowSnackbar(
                message = stringProvider.getString(VglsStringId.SNACKBAR_SCREEN_OFF),
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
