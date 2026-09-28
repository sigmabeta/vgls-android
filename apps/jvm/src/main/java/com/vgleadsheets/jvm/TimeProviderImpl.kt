package com.vgleadsheets.jvm

import kotlinx.datetime.LocalDate
import net.sigmabeta.sage.time.TimeProvider
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Instant

/** Desktop [TimeProvider] — locale-aware date formatting via java.time (same as the Android impl). */
class TimeProviderImpl : TimeProvider {
    override fun now(): Instant = Clock.System.now()

    override fun localDateFromString(date: String): LocalDate? = runCatching { LocalDate.parse(date) }.getOrNull()

    override fun longDateTextFromMillis(timestamp: Long): String? {
        val ms = if (timestamp == 0L) System.currentTimeMillis() else timestamp
        return LONG_DATE.format(java.time.Instant.ofEpochMilli(ms))
    }

    override fun longDateTimeText(instant: Instant): String =
        MEDIUM_DATE_TIME.format(java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds()))

    private companion object {
        private val LONG_DATE: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        private val MEDIUM_DATE_TIME: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
}
