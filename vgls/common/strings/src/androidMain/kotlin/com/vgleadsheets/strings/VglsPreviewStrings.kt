package com.vgleadsheets.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.vgleadsheets.strings.generated.resources.Res
import com.vgleadsheets.strings.generated.resources.allStringResources
import net.sigmabeta.sage.ui.SageStringId
import net.sigmabeta.sage.ui.StringProvider
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.LocalResourceReader
import org.jetbrains.compose.resources.MissingResourceException
import org.jetbrains.compose.resources.ResourceReader
import org.jetbrains.compose.resources.stringResource
import java.io.InputStream

/**
 * Compose-side [VglsStringProvider] for `@Preview` / Paparazzi (Android only). Reads each string
 * through the `@Composable` `stringResource` API — the same Compose-resource source the apps use —
 * so previews resolve real text without a `suspend`/`runBlocking` preload.
 *
 * Android Studio's Compose Preview renders under Layoutlib, whose classloader doesn't expose the
 * strings AAR's java-resources to `ClassLoader.getResourceAsStream` — so a `.cvr` lookup throws
 * `MissingResourceException` mid-composition. Compose forbids try/catch around composable
 * invocations, so we probe the classpath ONCE here and fall back to placeholder text (the enum
 * name) when the resources aren't reachable. The running apps never reach here; they preload via
 * [loadVglsStrings].
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun rememberVglsStringProvider(): StringProvider {
    val resourcesReachable = remember { hasComposeStringsOnClasspath() }
    if (!resourcesReachable) {
        return remember {
            val placeholders = mutableMapOf<SageStringId, String>()
            for (id in VglsStringId.entries) {
                placeholders[id] = id.name
            }
            VglsStringProvider(placeholders)
        }
    }

    lateinit var provider: StringProvider
    CompositionLocalProvider(LocalResourceReader provides ClasspathResourceReader) {
        val strings = mutableMapOf<SageStringId, String>()
        for (id in VglsStringId.entries) {
            strings[id] = stringResource(Res.allStringResources.getValue(id.name.lowercase()))
        }
        provider = VglsStringProvider(strings)
    }
    return provider
}

/** Single .cvr we probe before composition decides which provider to install. */
private const val SENTINEL_RESOURCE_PATH =
    "composeResources/com.vgleadsheets.strings.generated.resources/values/strings.commonMain.cvr"

private fun hasComposeStringsOnClasspath(): Boolean =
    ClasspathResourceReader::class.java.classLoader
        ?.getResource(SENTINEL_RESOURCE_PATH) != null

/**
 * A [ResourceReader] that reads Compose resources purely from the JVM classpath, with no Android
 * `Context` — the form the packaged `composeResources` `.cvr` files take on the Paparazzi and
 * unit-test runtime classpath.
 */
@OptIn(ExperimentalResourceApi::class)
private object ClasspathResourceReader : ResourceReader {
    override suspend fun read(path: String): ByteArray = open(path).use { it.readBytes() }

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray =
        open(path).use { input ->
            var skipped = 0L
            while (skipped < offset) {
                val count = input.skip(offset - skipped)
                if (count <= 0L) break
                skipped += count
            }
            val result = ByteArray(size.toInt())
            var read = 0
            while (read < result.size) {
                val count = input.read(result, read, result.size - read)
                if (count <= 0) break
                read += count
            }
            result
        }

    override fun getUri(path: String): String =
        loader().getResource(path)?.toURI()?.toString() ?: throw MissingResourceException(path)

    private fun open(path: String): InputStream =
        loader().getResourceAsStream(path) ?: throw MissingResourceException(path)

    private fun loader(): ClassLoader =
        javaClass.classLoader ?: error("No classloader available for Compose resource reading")
}
