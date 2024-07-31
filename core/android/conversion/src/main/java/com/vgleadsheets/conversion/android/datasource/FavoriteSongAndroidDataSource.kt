package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.database.android.dao.FavoriteSongRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.FavoriteSongEntity
import com.vgleadsheets.database.source.FavoriteSongDataSource
import kotlinx.coroutines.flow.map

class FavoriteSongAndroidDataSource(
    private val roomImpl: FavoriteSongRoomDao,
) : FavoriteSongDataSource {
    override suspend fun addFavorite(id: Long) {
        roomImpl.insert(
            FavoriteSongEntity(id)
        )
    }

    override suspend fun removeFavorite(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isFavoriteSong(id: Long) = roomImpl
        .getFavoriteSong(id)
        .map { it != null }
}
