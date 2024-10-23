package com.vgleadsheets.di

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.vgleadsheets.time.ThreeTenTime
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.Locale

class ThreeTenImpl(
    private val context: Context,
) : ThreeTenTime {

    private val initializer: Long by lazy {
        AndroidThreeTen.init(context)
        0L
    }

    private inline fun <T> initialized(crossinline func: () -> T): T {
        // This rigmarole is so the Kotlin compiler doesn't think the
        // let call is redundant. It be like that sometimes.
        return initializer.let {
            if (it == 0L) {
                func()
            } else {
                func()
            }
        }
    }

    override fun now(): ZonedDateTime = initialized { ZonedDateTime.now() }

    override fun parse(textToParse: String, formatter: DateTimeFormatter): ZonedDateTime = initialized {
        ZonedDateTime.parse(textToParse, formatter)
    }

    override fun zoneIdFrom(stringId: String): ZoneId = initialized {
        ZoneId.of(stringId)
    }

    override fun localDateFromString(date: String) = initialized {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        LocalDate.parse(date, formatter)
    }

    override fun longDateTextFromMillis(timestamp: Long) = initialized {
        val formatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        val instant = if (timestamp == 0L) Instant.now() else Instant.ofEpochMilli(timestamp)
        formatter.format(instant)
    }
}
