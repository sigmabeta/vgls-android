package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.model.Composer

class ComposerConverter : Converter<Composer, ComposerEntity> {
    override fun Composer.toEntity() = ComposerEntity(
        id,
        name,
        songCount,
        hasVocalSongs,
        photoUrl,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline
    )

    override fun ComposerEntity.toModel() = Composer(
        id,
        name,
        null,
        songCount,
        photoUrl,
        hasVocalSongs,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline,
    )
}
