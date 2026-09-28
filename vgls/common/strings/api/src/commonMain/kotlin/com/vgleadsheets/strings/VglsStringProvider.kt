package com.vgleadsheets.strings

import com.vgleadsheets.strings.generated.resources.Res
import com.vgleadsheets.strings.generated.resources.allStringResources
import net.sigmabeta.sage.ui.SageStringId
import net.sigmabeta.sage.ui.StringProvider
import org.jetbrains.compose.resources.getString

/**
 * The single, multiplatform [StringProvider]. String values live in one place —
 * `src/commonMain/composeResources/values/` — and Compose Multiplatform generates the accessors.
 * [loadVglsStrings] preloads every [VglsStringId]'s text once at startup (Compose's `getString` is
 * `suspend`) into a map, so the synchronous `StringProvider` calls used in non-composable view
 * models resolve without suspension. Replaces the per-platform Android `R.string` +
 * `AndroidStringProvider` path.
 */
class VglsStringProvider(private val strings: Map<SageStringId, String>) : StringProvider {
    override fun getString(string: SageStringId): String = strings[string] ?: error("No string mapping for $string")

    override fun getStringOneArg(string: SageStringId, arg: String): String = formatVglsString(getString(string), listOf(arg))

    override fun getStringOneInt(string: SageStringId, arg: Int): String = formatVglsString(getString(string), listOf(arg.toString()))

    override fun getStringTwoArgs(string: SageStringId, first: String, second: String): String = formatVglsString(getString(string), listOf(first, second))
}

/**
 * Preload every [VglsStringId]'s text from the Compose resources. Call once at startup (it's
 * `suspend`) and hand the result to [VglsStringProvider]. Each id maps to the resource whose name
 * is the lowercased enum name (matching the `<string name=...>` keys in the XML).
 */
suspend fun loadVglsStrings(): Map<SageStringId, String> {
    val strings = mutableMapOf<SageStringId, String>()
    for (id in VglsStringId.entries) {
        strings[id] = getString(Res.allStringResources.getValue(id.name.lowercase()))
    }
    return strings
}

/**
 * Multiplatform stand-in for `String.format` over Android-style specifiers (the only ones the
 * strings use): positional `%1$s`/`%2$s`/`%1$d`, then non-positional `%s`/`%d` filled in order.
 */
internal fun formatVglsString(template: String, args: List<String>): String {
    var result = template
    args.forEachIndexed { index, arg ->
        result = result.replace("%${index + 1}\$s", arg).replace("%${index + 1}\$d", arg)
    }
    var argIndex = 0
    val out = StringBuilder(result.length)
    var i = 0
    while (i < result.length) {
        val isSpecifier = result[i] == '%' &&
            i + 1 < result.length &&
            (result[i + 1] == 's' || result[i + 1] == 'd') &&
            argIndex < args.size
        if (isSpecifier) {
            out.append(args[argIndex])
            argIndex++
            i += 2
        } else {
            out.append(result[i])
            i++
        }
    }
    return out.toString()
}
