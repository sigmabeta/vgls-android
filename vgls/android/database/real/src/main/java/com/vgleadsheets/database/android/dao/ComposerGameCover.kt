package com.vgleadsheets.database.android.dao

/**
 * One game a composer is credited on, with that game's art; a row of
 * [ComposerRoomDao.getGameCovers]. At least one of [photoUrl] and [igdbImageId] is non-null.
 */
data class ComposerGameCover(
    val composerId: Long,
    val photoUrl: String?,
    val igdbImageId: String?,
)
