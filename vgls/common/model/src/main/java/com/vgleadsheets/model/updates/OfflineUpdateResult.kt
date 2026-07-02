package com.vgleadsheets.model.updates

import kotlin.time.Instant

data class OfflineUpdateResult(
    val id: Int,
    val dateTime: Instant,
    val serverUpdateTime: Instant,
    val updatedSongs: Int,
    val successfulOfflines: Int,
    val status: OfflineJobStatus,
)
