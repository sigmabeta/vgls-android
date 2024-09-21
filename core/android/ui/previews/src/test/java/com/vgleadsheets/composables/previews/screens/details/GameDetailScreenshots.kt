package com.vgleadsheets.composables.previews.screens.details

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.vgleadsheets.composables.previews.PreviewTestUtils.INTERESTING_DEVICES
import com.vgleadsheets.composables.previews.PreviewTestUtils.SUFFIX_TESTNAME
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GameDetailScreenshots(
    private val deviceConfig: DeviceConfig,
    private val category: String,
    private val width: String,
    private val height: String,
) {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun gameDetailScreenLight() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            GameDetailLight()
        }
    }

    @Test
    fun gameDetailScreenDark() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            GameDetailDark()
        }
    }

    @Test
    fun gameDetailScreenLightLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            GameDetailLoading()
        }
    }

    @Test
    fun gameDetailScreenDarkLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            GameDetailLoadingDark()
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = SUFFIX_TESTNAME)
        fun getDeviceConfig(): Iterable<Array<Any>> {
            return INTERESTING_DEVICES
        }
    }
}
