package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.OfflineGameEntity
import com.vgleadsheets.model.history.Offline

class OfflineGameConverter : Converter<Offline, OfflineGameEntity> {
    override fun Offline.toEntity() = OfflineGameEntity(id)

    override fun OfflineGameEntity.toModel() = Offline(id)
}

