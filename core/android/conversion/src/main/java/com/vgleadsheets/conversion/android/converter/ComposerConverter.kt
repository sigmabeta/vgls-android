package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.SongsForComposerDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals

class ComposerConverter :
    WithManyConverter<Composer, ComposerEntity, Song, SongEntity, SongsForComposerDao> {
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
        photoUrl,
        hasVocalSongs
    )

    override fun ComposerEntity.toModelWithMany(
        manyDao: SongsForComposerDao,
        converter: Converter<Song, SongEntity>
    ) = Composer(
        id,
        name,
        manyDao.getManyModels(id, converter),
        photoUrl,
        hasVocalSongs
    )

    override fun SongsForComposerDao.getManyModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getJoinedEntitiesSync(relationId)
        .map { converter.entityToModel(it) }
}
