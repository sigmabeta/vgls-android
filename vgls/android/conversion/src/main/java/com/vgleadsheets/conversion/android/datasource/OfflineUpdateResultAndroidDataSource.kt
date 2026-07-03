package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.OfflineUpdateResultConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.OfflineUpdateRoomDao
import com.vgleadsheets.database.source.OfflineUpdateResultDataSource
import com.vgleadsheets.model.updates.OfflineUpdateResult

class OfflineUpdateResultAndroidDataSource(
    private val roomImpl: OfflineUpdateRoomDao,
    private val converter: OfflineUpdateResultConverter,
) : OfflineUpdateResultDataSource {
    override suspend fun insert(entity: OfflineUpdateResult) = roomImpl.insert(converter.modelToEntity(entity))

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo { converter.entityToModel(it) }

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
