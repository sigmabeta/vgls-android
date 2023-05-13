package com.vgleadsheets.database.android

object DatabaseVersions {
    /**
     * - Added "sheetsPlayed" field to games.
     * - Added "sheetsPlayed" field to composers.
     */
    const val ADDED_PLAY_COUNTS = 11

    /**
     * - Removed Jams
     * - Removed Setlist entries
     * - Removed Song History entries
     */
    const val REMOVED_JAMS = 12

    /**
     *  - Added "isFavorite" field to songs.
     *  - Added "isFavorite" field to games.
     *  - Added "isFavorite" field to composers.
     *  - Added "isAvailableOffline" field to songs.
     *  - Added "isAvailableOffline" field to games.
     *  - Added "isAvailableOffline" field to composers.
     */
    const val ADDED_FAVORITES = 13

    /**
     *  - Added "altPageCount" to songs.
     *  - Added "isAltSelected" to songs.
     */
    const val ADDED_ALTERNATES = 14

    // Doesn't need to be changed.
    val WITHOUT_MIGRATION = (1 until ADDED_PLAY_COUNTS).toList().toIntArray()
}
