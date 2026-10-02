package com.vgleadsheets.composables.previews.components

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

class CollageImageScreenshots {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun collageImagesLight() {
        paparazzi.snapshot { CollageImages(darkTheme = false) }
    }

    @Test
    fun collageImagesDark() {
        paparazzi.snapshot { CollageImages(darkTheme = true) }
    }
}
