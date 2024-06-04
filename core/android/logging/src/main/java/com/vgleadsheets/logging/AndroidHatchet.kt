package com.vgleadsheets.logging

import android.util.Log
import java.util.regex.Pattern

class AndroidHatchet : Hatchet {
    override fun v(message: String) {
        log(Log.VERBOSE, message)
    }

    override fun d(message: String) {
        log(Log.DEBUG, message)
    }

    override fun i(message: String) {
        log(Log.INFO, message)
    }

    override fun w(message: String) {
        log(Log.WARN, message)
    }

    override fun e(message: String) {
        log(Log.ERROR, message)
    }

    override fun log(severity: Int, message: String) {
        logInternal(severity, message)
    }

    /**
     * Break up `message` into maximum-length chunks (if needed) and send to either
     * [Log.println()][Log.println] or
     * [Log.wtf()][Log.wtf] for logging.
     *
     * {@inheritDoc}
     */
    private fun logInternal(severity: Int, message: String) {
        if (!BuildConfig.DEBUG) {
            return
        }

        val threadName = Thread.currentThread().name
        val string = "Thr: $threadName | Msg: $message"

        if (string.length < MAX_LOG_LENGTH) {
            if (severity == Log.ASSERT) {
                Log.wtf(tag, string)
            } else {
                Log.println(severity, tag, string)
            }
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var index = 0
        val length = string.length

        while (index < length) {
            var newline = string.indexOf('\n', index)
            newline = if (newline != -1) newline else length

            do {
                val end = newline.coerceAtMost(index + MAX_LOG_LENGTH)
                val part = string.substring(index, end)

                if (severity == Log.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(severity, tag, part)
                }

                index = end
            } while (index < newline)

            index++
        }
    }

    private val fqcnIgnore = listOf(
        Hatchet::class.java.name,
        AndroidHatchet::class.java.name,
    )

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private val tag: String?
        get() = Throwable().stackTrace
            .first { it.className !in fqcnIgnore }
            .let(::createStackElementTag)

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     *
     * Note: This will not be called if a [manual tag][.tag] was specified.
     */
    private fun createStackElementTag(element: StackTraceElement): String? {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }

        return if (tag.length <= MAX_TAG_LENGTH) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val MAX_TAG_LENGTH = 23
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }
}
