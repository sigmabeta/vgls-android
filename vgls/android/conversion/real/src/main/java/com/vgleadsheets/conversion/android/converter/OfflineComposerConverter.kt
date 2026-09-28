package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.OfflineComposerEntity
import com.vgleadsheets.model.history.Offline

class OfflineComposerConverter : Converter<Offline, OfflineComposerEntity> {
    override fun Offline.toEntity() = OfflineComposerEntity(id)

    override fun OfflineComposerEntity.toModel() = Offline(id)
}
