package com.vgleadsheets.database.android

object UserContentDatabaseVersions {
    const val ORIGINAL = 1

    /**
     * - Added "offline_song" table.
     * - Added "offline_game" table.     // TODO
     * - Added "offline_composer" table.
     */
    const val ADDED_OFFLINE_SONGS = 2
    const val ADDED_OFFLINE_COMPOSERS = 3
}
