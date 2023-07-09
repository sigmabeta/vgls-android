package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Song

class SongConverter : Converter<Song, SongEntity> {
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
        null,
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
}
