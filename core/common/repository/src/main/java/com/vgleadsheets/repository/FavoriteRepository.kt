package com.vgleadsheets.repository

import com.vgleadsheets.database.source.FavoriteSongDataSource

class FavoriteRepository(
    private val favoriteSongDataSource: FavoriteSongDataSource,
) {
    suspend fun addFavorite(id: Long) {
        favoriteSongDataSource.addFavorite(id)
    }

    suspend fun removeFavorite(id: Long) {
        favoriteSongDataSource.removeFavorite(id)
    }

    fun isFavoriteSong(id: Long) = favoriteSongDataSource.isFavoriteSong(id)
}
