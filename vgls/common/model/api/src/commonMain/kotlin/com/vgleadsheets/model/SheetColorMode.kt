package com.vgleadsheets.model

/**
 * User-selectable color scheme for sheet music. The downloaded PDFs carry only ink (black glyphs,
 * staff lines, text) on a transparent background, so the app supplies the "paper" fill; [DARK]
 * additionally reverses the ink so black glyphs read as white on the black paper.
 *
 * [ordinal] is persisted by index, so the entries must not be reordered without a migration.
 */
enum class SheetColorMode {
    LIGHT,
    DARK,
    GENTLE,
}
