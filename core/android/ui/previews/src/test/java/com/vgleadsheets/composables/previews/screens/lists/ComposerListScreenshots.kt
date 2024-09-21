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
class ComposerListScreenshots(
    private val deviceConfig: DeviceConfig,
    private val category: String,
    private val width: String,
    private val height: String,
) {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun composerListScreenLight() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ComposerListLight()
        }
    }

    @Test
    fun composerListScreenDark() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ComposerListDark()
        }
    }

    @Test
    fun composerListScreenLightLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ComposerListLightLoading()
        }
    }

    @Test
    fun composerListScreenDarkLoading() {
        paparazzi.unsafeUpdateConfig(deviceConfig = deviceConfig)
        paparazzi.snapshot {
            ComposerListDarkLoading()
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