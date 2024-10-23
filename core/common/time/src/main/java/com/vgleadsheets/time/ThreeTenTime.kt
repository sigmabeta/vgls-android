package com.vgleadsheets.time

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

interface ThreeTenTime {
    fun now(): ZonedDateTime

    fun parse(textToParse: String, formatter: DateTimeFormatter): ZonedDateTime

    fun zoneIdFrom(stringId: String): ZoneId

    fun localDateFromString(date: String): LocalDate?

    fun longDateTextFromMillis(timestamp: Long): String?
}
