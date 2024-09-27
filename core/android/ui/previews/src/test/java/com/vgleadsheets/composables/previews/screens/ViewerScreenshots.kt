package com.vgleadsheets.composables.previews.screens

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.ScreenOrientation
import com.vgleadsheets.composables.previews.PreviewTestUtils.INTERESTING_DEVICES
import com.vgleadsheets.composables.previews.PreviewTestUtils.SUFFIX_TESTNAME
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ViewerScreenshots(
    private val deviceConfig: DeviceConfig,
    private val category: String,
    private val width: String,
    private val height: String,
) {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun viewerScreen() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            Viewer()
        }
    }

    @Test
    fun viewerScreenLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ViewerLoading()
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = SUFFIX_TESTNAME)
        fun getDeviceConfig(): Iterable<Array<Any>> {
            val landscapeDevices = INTERESTING_DEVICES.map { deviceConfigQuad ->
                val originalConfigName = deviceConfigQuad[1] as String
                val originalConfig = deviceConfigQuad[0] as DeviceConfig

                val landscapeConfigName = "$originalConfigName (Landscape)"
                val landscapeConfig = originalConfig.copy(
                    orientation = ScreenOrientation.LANDSCAPE,
                )

                arrayOf(
                    landscapeConfig,
                    landscapeConfigName,
                    deviceConfigQuad[2],
                    deviceConfigQuad[3],
                )
            }
            val portraitDevices = INTERESTING_DEVICES.map { deviceConfigQuad ->
                val originalConfigName = deviceConfigQuad[1] as String
                val originalConfig = deviceConfigQuad[0] as DeviceConfig

                val portraitConfigName = "$originalConfigName (Portrait)"
                val portraitConfig = originalConfig.copy(
                    orientation = ScreenOrientation.PORTRAIT
                )

                arrayOf(
                    portraitConfig,
                    portraitConfigName,
                    deviceConfigQuad[2],
                    deviceConfigQuad[3],
                )
            }

            return landscapeDevices + portraitDevices
        }
    }
}
