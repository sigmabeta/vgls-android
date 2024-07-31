package com.vgleadsheets.repository

import com.vgleadsheets.database.source.FavoriteComposerDataSource
import com.vgleadsheets.database.source.FavoriteSongDataSource

class FavoriteRepository(
    private val favoriteSongDataSource: FavoriteSongDataSource,
    private val favoriteComposerDataSource: FavoriteComposerDataSource,
) {
    suspend fun addFavoriteSong(id: Long) {
        favoriteSongDataSource.addFavorite(id)
    }

    suspend fun removeFavoriteSong(id: Long) {
        favoriteSongDataSource.removeFavorite(id)
    }

    fun isFavoriteSong(id: Long) = favoriteSongDataSource.isFavoriteSong(id)

    suspend fun addFavoriteComposer(id: Long) {
        favoriteComposerDataSource.addFavorite(id)
    }

    suspend fun removeFavoriteComposer(id: Long) {
        favoriteComposerDataSource.removeFavorite(id)
    }

    fun isFavoriteComposer(id: Long) = favoriteComposerDataSource.isFavoriteComposer(id)
}
