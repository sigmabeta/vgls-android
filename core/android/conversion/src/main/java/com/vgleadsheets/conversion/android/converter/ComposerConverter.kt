package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.filteredForVocals

class ComposerConverter : Converter<Composer, ComposerEntity> {
    override fun Composer.toEntity() = ComposerEntity(
        id,
        name,
        songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
        photoUrl,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline
    )

    override fun ComposerEntity.toModel() = Composer(
        id,
        name,
        null,
        photoUrl,
        hasVocalSongs,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline,
    )
}
