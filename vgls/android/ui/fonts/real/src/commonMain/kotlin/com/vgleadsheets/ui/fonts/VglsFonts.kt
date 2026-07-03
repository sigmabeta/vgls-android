package com.vgleadsheets.ui.fonts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.vgleadsheets.ui.fonts.real.generated.resources.Res
import com.vgleadsheets.ui.fonts.real.generated.resources.musejazz_text
import org.jetbrains.compose.resources.Font

/**
 * The MuseJazz Text brand face, loaded from the shared Compose Multiplatform font resource so it
 * renders identically on Android and desktop.
 *
 * `@Composable` because CMP's `Font(FontResource)` factory is — font loading is lazy and bound to
 * the composition. Callers (the theme) build their [androidx.compose.material3.Typography] with this
 * inside a composable, e.g. `AppTheme`.
 */
@Composable
fun museJazzFontFamily(): FontFamily = FontFamily(Font(Res.font.musejazz_text))
