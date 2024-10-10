package com.vgleadsheets.time

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.Locale

object PublishDateUtils {
    fun ldtFromString(date: String): LocalDate {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(date, formatter)
    }

    fun Long.toLongDateText(): String {
        val instant = Instant.ofEpochMilli(this)
        val formatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        if (this == 0L) return formatter.format(Instant.now())

        return formatter.format(instant)
    }
}
