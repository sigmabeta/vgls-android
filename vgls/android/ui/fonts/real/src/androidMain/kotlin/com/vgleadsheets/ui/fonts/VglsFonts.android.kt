package com.vgleadsheets.ui.fonts

import android.content.Context
import android.graphics.Typeface

// Android Canvas Typeface for the same MuseJazz composeResources .otf — used by the bitmap
// generators that draw placeholder text with Paint (a non-Compose path, so it can't use the
// @Composable museJazzFontFamily()). CMP packages commonMain composeResources as Android assets at
// `composeResources/<packageOfResClass>/font/<file>`, so load the Typeface from there — one shared
// font file, no res/font duplicate.
private const val MUSEJAZZ_ASSET_PATH =
    "composeResources/com.vgleadsheets.ui.fonts.real.generated.resources/font/musejazz_text.otf"

fun museJazzTypeface(context: Context): Typeface = runCatching { Typeface.createFromAsset(context.assets, MUSEJAZZ_ASSET_PATH) }
        .getOrDefault(Typeface.DEFAULT)
