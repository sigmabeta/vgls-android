package com.vgleadsheets.pdf.fake

import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.pdf.StandaloneReader

class FakeStandaloneReader(

) : StandaloneReader {
    override suspend fun renderToDrawable(data: PdfConfigById, drawableWidth: Int, drawableHeight: Int, resources: Resources): Drawable {
        TODO("Not yet implemented")
    }
}
