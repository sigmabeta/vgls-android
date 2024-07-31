package com.vgleadsheets.database.source

import kotlinx.coroutines.flow.Flow

interface FavoriteGameDataSource {
    suspend fun addFavorite(id: Long)

    suspend fun removeFavorite(id: Long)

    fun isFavoriteGame(id: Long): Flow<Boolean>
}
