package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.enitity.ComposerAliasEntity
import com.vgleadsheets.model.alias.ComposerAlias

class ComposerAliasConverter :
    Converter<ComposerAlias, ComposerAliasEntity> {
    override fun ComposerAlias.toEntity() = ComposerAliasEntity(
        id,
        name,
        photoUrl
    )

    override fun ComposerAliasEntity.toModel() = ComposerAlias(
        id ?: -1L,
        composerId,
        name,
        photoUrl
    )
}
