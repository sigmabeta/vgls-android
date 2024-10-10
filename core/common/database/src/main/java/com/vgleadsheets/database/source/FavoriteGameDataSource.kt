package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteGameDataSource {
    suspend fun addFavorite(id: Long)

    suspend fun removeFavorite(id: Long)

    fun isFavoriteGame(id: Long): Flow<Boolean>

    fun getAll(): Flow<List<Favorite>>
}
