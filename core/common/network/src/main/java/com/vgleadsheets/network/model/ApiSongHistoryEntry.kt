package com.vgleadsheets.network.model

import com.vgleadsheets.model.SongHistoryEntry

@Suppress("ConstructorParameterNaming")
data class ApiSongHistoryEntry(
    val sheet_id: Long
)

fun ApiSongHistoryEntry.toSongHistoryEntry(jamId: Long, listPosition: Int) = SongHistoryEntry(
    MULTIPLIER_JAM_ID * jamId + listPosition,
    null
)

const val MULTIPLIER_JAM_ID = 10000L
