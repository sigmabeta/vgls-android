package com.vgleadsheets.database.android

object DatabaseVersions {
    /**
     * - Added "sheetsPlayed" field to games.
     * - Added "sheetsPlayed" field to composers.
     */
    const val ADDED_PLAY_COUNTS = 11

    /**
     *  - Added "isFavorite" field to songs.
     *  - Added "isFavorite" field to games.
     *  - Added "isFavorite" field to composers.
     *  - Added "isAvailableOffline" field to songs.
     *  - Added "isAvailableOffline" field to games.
     *  - Added "isAvailableOffline" field to composers.
     */
    const val ADDED_FAVORITES = 12

    // Doesn't need to be changed.
    val WITHOUT_MIGRATION = (1 until ADDED_PLAY_COUNTS).toList().toIntArray()
}
