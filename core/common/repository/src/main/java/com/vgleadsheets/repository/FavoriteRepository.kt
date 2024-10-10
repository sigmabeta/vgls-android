package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.FavoriteComposerDataSource
import com.vgleadsheets.database.source.FavoriteGameDataSource
import com.vgleadsheets.database.source.FavoriteSongDataSource

class FavoriteRepository(
    private val songDataSource: SongDataSource,
    private val gameDataSource: GameDataSource,
    private val composerDataSource: ComposerDataSource,
    private val favoriteSongDataSource: FavoriteSongDataSource,
    private val favoriteGameDataSource: FavoriteGameDataSource,
    private val favoriteComposerDataSource: FavoriteComposerDataSource,
) {
    suspend fun addFavoriteSong(id: Long) {
        favoriteSongDataSource.addFavorite(id)
    }

    suspend fun addFavoriteGame(id: Long) {
        favoriteGameDataSource.addFavorite(id)
    }

    suspend fun addFavoriteComposer(id: Long) {
        favoriteComposerDataSource.addFavorite(id)
    }

    suspend fun removeFavoriteSong(id: Long) {
        favoriteSongDataSource.removeFavorite(id)
    }

    suspend fun removeFavoriteGame(id: Long) {
        favoriteGameDataSource.removeFavorite(id)
    }

    suspend fun removeFavoriteComposer(id: Long) {
        favoriteComposerDataSource.removeFavorite(id)
    }

    fun getAllSongs() = favoriteSongDataSource
        .getAll()
        .mapListTo {
            songDataSource.getOneByIdSync(it.id)
        }

    fun getAllGames() = favoriteGameDataSource
        .getAll()
        .mapListTo {
            gameDataSource.getOneByIdSync(it.id)
        }

    fun getAllComposers() = favoriteComposerDataSource
        .getAll()
        .mapListTo {
            composerDataSource.getOneByIdSync(it.id)
        }

    fun isFavoriteSong(id: Long) = favoriteSongDataSource.isFavoriteSong(id)

    fun isFavoriteComposer(id: Long) = favoriteComposerDataSource.isFavoriteComposer(id)

    fun isFavoriteGame(id: Long) = favoriteGameDataSource.isFavoriteGame(id)
}
