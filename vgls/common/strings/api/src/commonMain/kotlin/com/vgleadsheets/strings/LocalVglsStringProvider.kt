package com.vgleadsheets.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import net.sigmabeta.sage.ui.StringProvider

/**
 * Composable access to the VGLS [StringProvider]. Provided once near the root of each platform's
 * Compose tree — `RemasteredActivity` on Android and `DesktopMain` on the JVM target both inject
 * the Metro-graph `StringProvider` (a [VglsStringProvider] backed by the single multiplatform
 * string source). Composables resolve a string via [text] / [textInt] instead of the Android-only
 * `stringResource` factory.
 */
val LocalVglsStringProvider = staticCompositionLocalOf<StringProvider> {
    error(
        "LocalVglsStringProvider not provided. Wrap your Compose tree in " +
            "CompositionLocalProvider(LocalVglsStringProvider provides yourStringProvider) { ... }",
    )
}

@Composable
@ReadOnlyComposable
fun VglsStringId.text(): String = LocalVglsStringProvider.current.getString(this)

@Composable
@ReadOnlyComposable
fun VglsStringId.text(arg: String): String = LocalVglsStringProvider.current.getStringOneArg(this, arg)

@Composable
@ReadOnlyComposable
fun VglsStringId.textInt(arg: Int): String = LocalVglsStringProvider.current.getStringOneInt(this, arg)

@Composable
@ReadOnlyComposable
fun VglsStringId.text(first: String, second: String): String = LocalVglsStringProvider.current.getStringTwoArgs(this, first, second)
