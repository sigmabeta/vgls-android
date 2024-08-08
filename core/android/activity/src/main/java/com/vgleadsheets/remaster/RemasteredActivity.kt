package com.vgleadsheets.remaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.scaffold.RemasterAppUi
import com.vgleadsheets.ui.themes.VglsMaterial
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RemasteredActivity :
    ComponentActivity() {

    @Inject
    lateinit var hatchet: Hatchet

    private val navViewModel: NavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEdgeToEdge()
        printDisplayDetails()

        val windowInsetController = WindowInsetsControllerCompat(window, window.decorView)

        val hideSystemBars = { windowInsetController.hide(WindowInsetsCompat.Type.systemBars()) }
        val showSystemBars = { windowInsetController.show(WindowInsetsCompat.Type.systemBars()) }

        setupIntentLauncher()

        setContent {
            VglsMaterial {
                RemasterAppUi(
                    showSystemBars,
                    hideSystemBars,
                    modifier = Modifier
                )
            }
        }
    }

    private fun setupIntentLauncher() {
        navViewModel.intents
            .onEach { startActivity(it) }
            .launchIn(navViewModel.viewModelScope)
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
    }

    private fun printDisplayDetails() {
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        hatchet.v("Device screen DPI: ${displayMetrics.densityDpi}")
        hatchet.v(

            "Device screen scaling factor: ${displayMetrics.density}"
        )
        hatchet.v("Device screen size: ${widthPixels}x$heightPixels")
        hatchet.v(

            "Device screen size (scaled): ${(widthPixels / displayMetrics.density).toInt()}" +
                "x${(heightPixels / displayMetrics.density).toInt()}"
        )
    }
}
