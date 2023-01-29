package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.alias.ComposerAlias

class ComposerAliasConverter :
    OneToOneConverter<ComposerAlias, ComposerAliasEntity, Composer, ComposerEntity, ComposerRoomDao> {
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

    override fun ComposerAliasEntity.toModelWithRelatedOne(
        foreignDao: ComposerRoomDao,
        converter: Converter<Composer, ComposerEntity>
    ) = ComposerAlias(
        id ?: -1L,
        composerId,
        name,
        foreignDao.getForeignModel(composerId, converter)
    )

    override fun ComposerRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Composer, ComposerEntity>
    ) = getOneByIdSync(foreignId)
        .let { converter.entityToModel(it) }
}
