package com.vgleadsheets.model.updates

import org.threeten.bp.ZonedDateTime

data class OfflineUpdateResult(
    val id: Int,
    val dateTime: ZonedDateTime,
    val serverUpdateTime: ZonedDateTime,
    val newSongs: Int,
    val newGames: Int,
    val newComposers: Int,
    val updatedSongs: Int,
    val successfulOfflines: Int,
)
