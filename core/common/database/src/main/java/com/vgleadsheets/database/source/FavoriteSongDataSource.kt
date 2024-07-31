package com.vgleadsheets.database.source

import kotlinx.coroutines.flow.Flow

interface FavoriteSongDataSource {
    suspend fun addFavorite(id: Long)

    suspend fun removeFavorite(id: Long)

    fun isFavoriteSong(id: Long): Flow<Boolean>
}
