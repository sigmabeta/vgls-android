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
    fun viewerScreenLight() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ViewerLight()
        }
    }

    @Test
    fun viewerScreenDark() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ViewerDark()
        }
    }

    @Test
    fun viewerScreenLightLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ViewerLightLoading()
        }
    }

    @Test
    fun viewerScreenDarkLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ViewerDarkLoading()
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
                    orientation = ScreenOrientation.LANDSCAPE
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

                val landscapeConfigName = "$originalConfigName (Portrait)"
                val landscapeConfig = originalConfig.copy(
                    orientation = ScreenOrientation.PORTRAIT
                )

                arrayOf(
                    landscapeConfig,
                    landscapeConfigName,
                    deviceConfigQuad[2],
                    deviceConfigQuad[3],
                )
            }

            return landscapeDevices + portraitDevices
        }
    }
}
