package com.vgleadsheets.network.model

@Suppress("ConstructorParameterNaming")
data class ApiTime(
    val last_updated: String
) {
    fun toEntity(
        TimeType.LAST_UPDATED.ordinal,
        Instant.parse(last_updated).toEpochMilli()
    )
}
