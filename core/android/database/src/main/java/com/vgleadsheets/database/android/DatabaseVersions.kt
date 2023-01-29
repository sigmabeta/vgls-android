package com.vgleadsheets.database.android

object DatabaseVersions {
    /**
     * - Added "sheetsPlayed" field to games.
     * - Added "sheetsPlayed" field to composers.
     */
    const val ADDED_PLAY_COUNTS = 11

    val WITHOUT_MIGRATION = (1 until ADDED_PLAY_COUNTS).toList().toIntArray()
}
