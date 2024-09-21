package com.vgleadsheets.composables.previews

import app.cash.paparazzi.DeviceConfig
import com.android.resources.Density

object PreviewTestUtils {
    internal const val SUFFIX_TESTNAME = "{1}: {2}x{3}"

    internal val INTERESTING_DEVICES = listOf(
        "Small Tablet" to DeviceConfig.NEXUS_7,
        "Beeg Tablet" to DeviceConfig.NEXUS_10,
        "Square Tablet" to DeviceConfig.NEXUS_10.copy(screenHeight = 768, screenWidth = 1024, density = Density.MEDIUM),
        "Small Phone" to DeviceConfig.NEXUS_4,
        "Mid Phone" to DeviceConfig.PIXEL_6,
        "Beeg Phone" to DeviceConfig.PIXEL_6_PRO,
    ).map { pair ->
        val category = pair.first
        val config = pair.second
        arrayOf(config, category, config.screenWidth.toString(), config.screenHeight.toString())
    }
}
