package com.vgleadsheets.remaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.nav.ActivityEvent
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.pdf.subsample.LocalPdfSubsampler
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import net.sigmabeta.sage.ui.perf.LocalLogger
import com.vgleadsheets.scaffold.RemasterAppUi
import com.vgleadsheets.scaffold.systemui.SystemUiState
import com.vgleadsheets.scaffold.systemui.SystemUiViewModel
import com.vgleadsheets.strings.LocalVglsStringProvider
import com.vgleadsheets.ui.theme.AppTheme
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class RemasteredActivity : ComponentActivity() {
    // The Application implements ActivityGraph; this replaces Hilt's @AndroidEntryPoint member injection.
    private val activityGraph: ActivityGraph get() = application as ActivityGraph

    private val hatchet: Hatchet get() = activityGraph.hatchet
    private val pdfSubsampleSourceFactory: PdfSubsampleSource.Factory
        get() = activityGraph.pdfSubsampleSourceFactory
    private val stringProvider get() = activityGraph.stringProvider

    private val navViewModel: NavViewModel by viewModels { activityGraph.metroViewModelFactory }

    private val systemUiViewModel: SystemUiViewModel by viewModels { activityGraph.metroViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this Activity so the (AppScope) wake-lock manager can toggle its window flags, and
        // touch the dependency initializer to preserve its former eager (Hilt-injected) side effects.
        activityGraph.bindActivity(this)
        activityGraph.activityDependencyInitializer

        installSplashScreen()
        setupEdgeToEdge()
        printDisplayDetails()

        val windowInsetController = WindowInsetsControllerCompat(window, window.decorView)

        setupNavEventListener()
        setupSystemUiListener(windowInsetController)

        setContent {
            AppTheme {
                CompositionLocalProvider(
                    LocalPdfSubsampler provides pdfSubsampleSourceFactory,
                    LocalMetroViewModelFactory provides activityGraph.metroViewModelFactory,
                    LocalVglsStringProvider provides stringProvider,
                ) {
                    CompositionLocalProvider(LocalLogger provides hatchet) {
                        RemasterAppUi(
                            modifier = Modifier
                        )
                        // PdfTestScreen(modifier = Modifier)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        activityGraph.unbindActivity()
        super.onDestroy()
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)

        // Required for API 34 and later
        // Ref: https://developer.android.com/about/versions/14/behavior-changes-14#safer-intents
        mainIntent.setPackage(packageName)
        startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    private fun setupNavEventListener() {
        navViewModel.activityEvents
            .receiveAsFlow()
            .onEach { handleNavEvent(it) }
            .launchIn(navViewModel.viewModelScope)
    }

    private fun setupSystemUiListener(windowInsetController: WindowInsetsControllerCompat) {
        systemUiViewModel.uiState
            .onEach { handleSystemUiState(it, windowInsetController) }
            .launchIn(systemUiViewModel.viewModelScope)
    }

    private fun handleNavEvent(event: ActivityEvent) {
        when (event) {
            ActivityEvent.Finish -> finish()
            is ActivityEvent.LaunchIntent -> startActivity(event.intent)
            ActivityEvent.Restart -> restartApp()
        }
    }

    private fun handleSystemUiState(state: SystemUiState, windowInsetController: WindowInsetsControllerCompat) {
        if (state.actualVisibility == SystemUiVisibility.VISIBLE) {
            windowInsetController.show(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
    }

    private fun printDisplayDetails() {
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        hatchet.v("Device screen DPI: ${displayMetrics.densityDpi}")
        hatchet.v("Device screen scaling factor: ${displayMetrics.density}")
        hatchet.v("Device screen size: ${widthPixels}x$heightPixels")
        hatchet.v(
            "Device screen size (scaled): ${(widthPixels / displayMetrics.density).roundToInt()}" +
                "x${(heightPixels / displayMetrics.density).roundToInt()}"
        )
    }
}
