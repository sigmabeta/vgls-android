package com.vgleadsheets.model.time

import java.util.*

@Suppress("ConstructorParameterNaming")
data class ApiTime(
    val last_updated: String
) {
    fun toTimeEntity(): TimeEntity {
        val split = last_updated.split("-")

        return TimeEntity(
            TimeType.LAST_UPDATED.ordinal,
            Calendar.getInstance().apply {
                val year = split[POSITION_YEAR].toInt()
                val month = split[POSITION_MONTH].toInt()
                val date = split[POSITION_DAY].toInt()
                set(
                    year,
                    month,
                    date
                )
            }.time.time
        )
    }

    companion object {
        const val POSITION_YEAR = 0
        const val POSITION_MONTH = 1
        const val POSITION_DAY = 2
    }
}
