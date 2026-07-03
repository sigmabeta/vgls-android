package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.SongAliasEntity
import com.vgleadsheets.model.alias.SongAlias

class SongAliasConverter : Converter<SongAlias, SongAliasEntity> {
    override fun SongAlias.toEntity() = SongAliasEntity(
        songId,
        name,
        id
    )

    override fun SongAliasEntity.toModel() = SongAlias(
        id ?: -1L,
        songId,
        name,
        null
    )
}
