package com.vgleadsheets.time

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object PublishDateUtils {
    fun ldtFromString(date: String): LocalDate {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(date, formatter)
    }
}
