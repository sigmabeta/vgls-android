package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.FavoriteComposerConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.FavoriteComposerRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.FavoriteComposerEntity
import com.vgleadsheets.database.source.FavoriteComposerDataSource
import kotlinx.coroutines.flow.map

class FavoriteComposerAndroidDataSource(
    private val roomImpl: FavoriteComposerRoomDao,
    private val converter: FavoriteComposerConverter
) : FavoriteComposerDataSource {
    override suspend fun addFavorite(id: Long) {
        roomImpl.insert(
            FavoriteComposerEntity(id)
        )
    }

    override suspend fun removeFavorite(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isFavoriteComposer(id: Long) = roomImpl
        .getFavoriteComposer(id)
        .map { it != null }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }
}
