package com.vgleadsheets.time

interface ThreeTenTime {

    fun now(): org.threeten.bp.ZonedDateTime

    fun parse(textToParse: String, formatter: org.threeten.bp.format.DateTimeFormatter): org.threeten.bp.ZonedDateTime

    fun zoneIdFrom(stringId: String): org.threeten.bp.ZoneId
}
