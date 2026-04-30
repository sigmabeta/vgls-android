package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.FavoriteGameConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.FavoriteGameRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.FavoriteGameEntity
import com.vgleadsheets.database.source.FavoriteGameDataSource
import kotlinx.coroutines.flow.map

class FavoriteGameAndroidDataSource(
    private val roomImpl: FavoriteGameRoomDao,
    private val converter: FavoriteGameConverter,
) : FavoriteGameDataSource {
    override suspend fun addFavorite(id: Long) {
        roomImpl.insert(
            FavoriteGameEntity(id)
        )
    }

    override suspend fun removeFavorite(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isFavoriteGame(id: Long) = roomImpl
        .getFavoriteGame(id)
        .map { it != null }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }
}
