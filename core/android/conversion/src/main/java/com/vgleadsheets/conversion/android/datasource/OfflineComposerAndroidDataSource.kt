package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.OfflineComposerConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.OfflineComposerRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.OfflineComposerEntity
import com.vgleadsheets.database.source.OfflineComposerDataSource
import kotlinx.coroutines.flow.map

class OfflineComposerAndroidDataSource(
    private val roomImpl: OfflineComposerRoomDao,
    private val converter: OfflineComposerConverter,
) : OfflineComposerDataSource {
    override suspend fun addOffline(id: Long) {
        roomImpl.insert(
            OfflineComposerEntity(id)
        )
    }

    override suspend fun removeOffline(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isOfflineComposer(id: Long) = roomImpl
        .getOfflineComposer(id)
        .map { it != null }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }
}
