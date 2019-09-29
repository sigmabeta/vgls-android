package com.vgleadsheets.model.time

import org.threeten.bp.format.DateTimeFormatter

@Suppress("ConstructorParameterNaming")
data class ApiTime(
    val last_updated: String
) {
    fun toTimeEntity(threeTen: ThreeTenTime): TimeEntity {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN)
        val updatedZonedTime = threeTen.parse(
            last_updated + DATE_SUFFIX,
            formatter
        )

        return TimeEntity(
            TimeType.LAST_UPDATED.ordinal,
            updatedZonedTime.toInstant().toEpochMilli()
        )
    }

    companion object {
        const val DATE_FORMATTER_PATTERN = "yyyy-MM-dd hh:mm:ss a VV"
        const val DATE_SUFFIX = " 10:00:00 PM America/New_York"
    }
}
