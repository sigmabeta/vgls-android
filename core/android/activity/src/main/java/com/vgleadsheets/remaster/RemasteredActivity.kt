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
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.ActivityEvent
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.pdf.subsample.LocalPdfSubsampler
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import com.vgleadsheets.perf.LocalLogger
import com.vgleadsheets.scaffold.RemasterAppUi
import com.vgleadsheets.scaffold.systemui.SystemUiState
import com.vgleadsheets.scaffold.systemui.SystemUiViewModel
import com.vgleadsheets.ui.themes.VglsMaterial
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class RemasteredActivity : ComponentActivity() {
    @Inject
    lateinit var hatchet: Hatchet

    @Inject
    lateinit var activityDependencyInitializer: ActivityDependencyInitializer

    @Inject
    lateinit var pdfSubsampleSourceFactory: PdfSubsampleSource.Factory

    private val navViewModel: NavViewModel by viewModels()

    private val systemUiViewModel: SystemUiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setupEdgeToEdge()
        printDisplayDetails()

        val windowInsetController = WindowInsetsControllerCompat(window, window.decorView)

        setupNavEventListener()
        setupSystemUiListener(windowInsetController)

        setContent {
            VglsMaterial {
                CompositionLocalProvider(LocalPdfSubsampler provides pdfSubsampleSourceFactory) {
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
