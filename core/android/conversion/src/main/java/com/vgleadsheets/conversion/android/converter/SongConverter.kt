package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.ComposersForSongDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song

class SongConverter :
    WithManyConverter<Song, SongEntity, Composer, ComposerEntity, ComposersForSongDao> {
    override fun toModelFromEntity(entity: SongEntity) = entity.toModel()

    override fun Song.toEntity() = SongEntity(
        id,
        name,
        filename,
        pageCount,
        altPageCount,
        lyricPageCount,
        gameName,
        gameId,
        hasVocals,
        playCount,
        isFavorite,
        isAvailableOffline,
        isAltSelected
    )

    override fun SongEntity.toModel() = Song(
        id,
        name,
        filename,
        game_id,
        gameName,
        hasVocals,
        pageCount,
        altPageCount,
        lyricPageCount,
        null,
        playCount,
        isFavorite,
        isAvailableOffline,
        isAltSelected
    )

    override fun SongEntity.toModelWithMany(
        manyDao: ComposersForSongDao,
        converter: Converter<Composer, ComposerEntity>
    ): Song {
        val composers = manyDao.getManyModels(id, converter)
        return Song(
            id,
            name,
            filename,
            game_id,
            gameName,
            hasVocals,
            pageCount,
            altPageCount,
            lyricPageCount,
            composers,
            playCount,
            isFavorite,
            isAvailableOffline,
            isAltSelected,
        )
    }

    override fun ComposersForSongDao.getManyModels(
        relationId: Long,
        converter: Converter<Composer, ComposerEntity>
    ) = getJoinedEntitiesSync(relationId)
        .map { converter.entityToModel(it) }
}
