package com.vgleadsheets.model.updates

import org.threeten.bp.ZonedDateTime

data class OfflineUpdateResult(
    val id: Int,
    val dateTime: ZonedDateTime,
    val serverUpdateTime: ZonedDateTime,
    val updatedSongs: Int,
    val successfulOfflines: Int,
    val status: OfflineJobStatus,
)
