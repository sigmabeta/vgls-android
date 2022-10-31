package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals

class ComposerConverter :
    WithManyConverter<Composer, ComposerEntity, Song, SongEntity, SongRoomDao> {
    override fun Composer.toEntity() = ComposerEntity(
        id,
        name,
        songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
        photoUrl
    )

    override fun ComposerEntity.toModel() = Composer(
        id,
        name,
        null,
        photoUrl
    )

    override fun ComposerEntity.toModelWithJoinedMany(
        manyDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = Composer(
        id,
        name,
        manyDao.getJoinedModels(id, converter),
        photoUrl
    )

    override fun SongRoomDao.getJoinedModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getJoinedEntitiesSync(relationId)
        .map { converter.entityToModel(it) }
}
