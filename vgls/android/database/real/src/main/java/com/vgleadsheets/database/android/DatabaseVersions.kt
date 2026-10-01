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

    /*
     *  - Added "songCount" to games.
     *  - Added "songCount" to composers.
     */
    const val ADDED_SONG_COUNTS = 15

    /**
     *  - Added offline_update table for tracking download job results.
     */
    const val ADDED_OFFLINE_UPDATE_RESULTS = 16

    /**
     *  - Added "lastModifiedOnServer" to songs.
     *  - Added "lastDownloaded" to songs.
     */
    const val ADDED_SONG_MODIFIED_TIMES = 17

    /**
     *  - Added "igdbImageId" to games.
     */
    const val ADDED_IGDB_IMAGE_IDS = 18

    /** The column added in [ADDED_IGDB_IMAGE_IDS], shared by the android and desktop migrations. */
    const val ADD_IGDB_IMAGE_ID_SQL = "ALTER TABLE game ADD COLUMN igdbImageId TEXT"

    // Doesn't need to be changed.
    val WITHOUT_MIGRATION = (1 until ADDED_PLAY_COUNTS).toList().toIntArray()
}
