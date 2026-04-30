package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteSongDataSource {
    suspend fun addFavorite(id: Long)

    suspend fun removeFavorite(id: Long)

    fun isFavoriteSong(id: Long): Flow<Boolean>

    fun getAll(): Flow<List<Favorite>>
}
