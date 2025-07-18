package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.entity.OfflineSongEntity
import com.vgleadsheets.model.history.Offline

class OfflineSongConverter : Converter<Offline, OfflineSongEntity> {
    override fun Offline.toEntity() = OfflineSongEntity(id)

    override fun OfflineSongEntity.toModel() = Offline(id)
}
