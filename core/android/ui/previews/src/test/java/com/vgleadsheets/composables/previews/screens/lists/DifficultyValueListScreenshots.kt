package com.vgleadsheets.composables.previews.screens.lists

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.vgleadsheets.composables.previews.PreviewTestUtils.INTERESTING_DEVICES
import com.vgleadsheets.composables.previews.PreviewTestUtils.SUFFIX_TESTNAME
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DifficultyValueListScreenshots(
    private val deviceConfig: DeviceConfig,
    private val category: String,
    private val width: String,
    private val height: String,
) {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun difficultyValueListScreenLight() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            DifficultyValueListLight()
        }
    }

    @Test
    fun difficultyValueListScreenDark() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            DifficultyValueListDark()
        }
    }

    @Test
    fun difficultyValueListScreenLightLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            DifficultyValueListLightLoading()
        }
    }

    @Test
    fun difficultyValueListScreenDarkLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            DifficultyValueListDarkLoading()
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
