package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.model.alias.ComposerAlias

class ComposerAliasConverter :
    Converter<ComposerAlias, ComposerAliasEntity> {
    override fun ComposerAlias.toEntity() = ComposerAliasEntity(
        composerId,
        name,
        id
    )

    override fun ComposerAliasEntity.toModel() = ComposerAlias(
        id ?: -1L,
        composerId,
        name,
        null
    )
}
