package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.OfflineSongConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.OfflineSongRoomDao
import com.vgleadsheets.database.android.entity.DeletionId
import com.vgleadsheets.database.android.entity.OfflineSongEntity
import com.vgleadsheets.database.source.OfflineSongDataSource
import kotlinx.coroutines.flow.map

class OfflineSongAndroidDataSource(
    private val roomImpl: OfflineSongRoomDao,
    private val converter: OfflineSongConverter,
) : OfflineSongDataSource {
    override suspend fun addOffline(id: Long) {
        roomImpl.insert(
            OfflineSongEntity(id)
        )
    }

    override suspend fun removeOffline(id: Long) {
        roomImpl.remove(
            listOf(DeletionId(id))
        )
    }

    override fun isOfflineSong(id: Long) = roomImpl
        .getOfflineSong(id)
        .map { it != null }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }
}
