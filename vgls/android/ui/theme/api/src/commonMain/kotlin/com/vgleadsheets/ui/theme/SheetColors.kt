@file:Suppress("MagicNumber")

package com.vgleadsheets.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import com.vgleadsheets.model.SheetColorMode

/**
 * The active sheet color scheme, provided at the app root from the persisted setting. Sheet
 * renderers read this to pick the "paper" fill and, for dark, reverse the ink.
 */
val LocalSheetColorMode = staticCompositionLocalOf { SheetColorMode.LIGHT }

/** The fill drawn behind the (transparent-background) PDF ink. */
val SheetColorMode.paperColor: Color
    get() = when (this) {
        SheetColorMode.LIGHT -> Color.White
        SheetColorMode.DARK -> Color.Black
        SheetColorMode.GENTLE -> Color(0xFFF1E6C8)
    }

/** The color the PDF ink should read as against [paperColor]. */
val SheetColorMode.contentColor: Color
    get() = when (this) {
        SheetColorMode.LIGHT, SheetColorMode.GENTLE -> Color.Black
        SheetColorMode.DARK -> Color.White
    }

/**
 * A filter that maps the rendered sheet's ink to the scheme's content color while preserving the
 * transparent paper, so the [paperColor] behind it shows through. PDFs render ink as black on
 * transparent (or white-on-transparent once the renderer bakes a paper fill), so:
 *
 * - [SheetColorMode.LIGHT] needs no filter (black ink on white paper).
 * - [SheetColorMode.DARK] reverses it (white ink on black paper).
 * - [SheetColorMode.GENTLE] tints black ink onto beige paper.
 *
 * Null means identity — skipping the filter entirely for the default scheme.
 */
val SheetColorMode.contentColorFilter: ColorFilter?
    get() = when (this) {
        SheetColorMode.LIGHT -> null
        SheetColorMode.DARK -> colorRemapFilter(content = Color.White, paper = Color.Black)
        SheetColorMode.GENTLE -> colorRemapFilter(content = Color.Black, paper = Color(0xFFF1E6C8))
    }

/**
 * Builds a [ColorFilter] mapping input white (`1`) to [paper] and input black (`0`) to [content]:
 * `out = content + in * (paper - content)`. Over a transparent page (input black, alpha 0) this
 * leaves the paper transparent; over a baked white page it recolors the paper.
 */
private fun colorRemapFilter(content: Color, paper: Color): ColorFilter {
    val values = floatArrayOf(
        paper.red - content.red, 0f, 0f, 0f, content.red * 255f,
        0f, paper.green - content.green, 0f, 0f, content.green * 255f,
        0f, 0f, paper.blue - content.blue, 0f, content.blue * 255f,
        0f, 0f, 0f, 1f, 0f,
    )
    return ColorFilter.colorMatrix(ColorMatrix(values))
}
