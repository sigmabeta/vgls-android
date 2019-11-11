package com.vgleadsheets.model.time

import org.threeten.bp.Instant

@Suppress("ConstructorParameterNaming")
data class ApiTime(
    val last_updated: String
) {
    fun toTimeEntity() = TimeEntity(
        TimeType.LAST_UPDATED.ordinal,
        Instant.parse(last_updated).toEpochMilli()
    )
}
