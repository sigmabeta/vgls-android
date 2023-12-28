package com.vgleadsheets.remaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.composables.NameCaptionListItem
import com.vgleadsheets.logging.Hatchet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RemasteredActivity :
    ComponentActivity() {

    @Inject
    lateinit var hatchet: Hatchet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEdgeToEdge()
        printDisplayDetails()

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                modifier = Modifier,
                startDestination = "home"
            ) {
                composable("home") {
                    NameCaptionListItem(
                        model = NameCaptionListModel(
                            dataId = 1234L,
                            name = "Just a home screen",
                            caption = "Nothing to see here",
                            onClick = { }
                        ),
                        modifier = Modifier
                    )
                }
            }
        }
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun printDisplayDetails() {
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        hatchet.v(this.javaClass.simpleName, "Device screen DPI: ${displayMetrics.densityDpi}")
        hatchet.v(
            this.javaClass.simpleName,
            "Device screen scaling factor: ${displayMetrics.density}"
        )
        hatchet.v(this.javaClass.simpleName, "Device screen size: ${widthPixels}x$heightPixels")
        hatchet.v(
            this.javaClass.simpleName,
            "Device screen size (scaled): ${(widthPixels / displayMetrics.density).toInt()}" +
                "x${(heightPixels / displayMetrics.density).toInt()}"
        )
    }
}
