package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.OfflineGameConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.OfflineGameRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.OfflineGameEntity
import com.vgleadsheets.database.source.OfflineGameDataSource
import kotlinx.coroutines.flow.map

class OfflineGameAndroidDataSource(
    private val roomImpl: OfflineGameRoomDao,
    private val converter: OfflineGameConverter,
) : OfflineGameDataSource {
    override suspend fun addOffline(id: Long) {
        roomImpl.insert(
            OfflineGameEntity(id)
        )
    }

    override suspend fun removeOffline(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isOfflineGame(id: Long) = roomImpl
        .getOfflineGame(id)
        .map { it != null }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }
}
