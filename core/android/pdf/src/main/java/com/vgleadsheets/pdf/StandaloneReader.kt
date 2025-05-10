package com.vgleadsheets.pdf

import android.content.res.Resources
import android.graphics.drawable.Drawable

interface StandaloneReader {
    suspend fun renderToDrawable(
        data: PdfConfigById,
        drawableWidth: Int,
        drawableHeight: Int,
        resources: Resources
    ): Drawable
}
