package com.vgleadsheets.composables.previews

import app.cash.paparazzi.DeviceConfig
import com.android.resources.Density
import com.android.resources.NightMode

object PreviewTestUtils {
    internal const val SUFFIX_TESTNAME = "{1}: {2}x{3}"

    private val INTERESTING_DEVICES_INTERNAL = listOf(
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

    internal val INTERESTING_DEVICES = INTERESTING_DEVICES_INTERNAL.map { deviceConfigQuad ->
        val originalConfigName = deviceConfigQuad[1] as String
        val originalConfig = deviceConfigQuad[0] as DeviceConfig

        val lightConfigName = "$originalConfigName (Light)"
        val lightConfig = originalConfig.copy(
            nightMode = NightMode.NOTNIGHT
        )

        arrayOf(
            lightConfig,
            lightConfigName,
            deviceConfigQuad[2],
            deviceConfigQuad[3],
        )
    } + INTERESTING_DEVICES_INTERNAL.map { deviceConfigQuad ->
        val originalConfigName = deviceConfigQuad[1] as String
        val originalConfig = deviceConfigQuad[0] as DeviceConfig

        val darkConfigName = "$originalConfigName (Dark)"
        val darkConfig = originalConfig.copy(
            nightMode = NightMode.NIGHT
        )

        arrayOf(
            darkConfig,
            darkConfigName,
            deviceConfigQuad[2],
            deviceConfigQuad[3],
        )
    }
}
