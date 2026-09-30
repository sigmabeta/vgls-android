package com.vgleadsheets.composables.previews

import androidx.compose.ui.unit.dp
import com.vgleadsheets.model.IgdbImages

/**
 * The hero image: in practice always a game's cover (composers have no photos), so it's
 * cover-shaped, capped in height and centered so it doesn't fill a phone screen.
 */
object BigImageConstants {
    const val ASPECT_RATIO = IgdbImages.COVER_ASPECT_RATIO
    val MAX_HEIGHT = 384.dp
    val MIN_WIDTH = 320.dp
}
