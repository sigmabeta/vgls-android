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
        isAvailableOffline,
        metadataPhotoUrl,
        metadataPhotoAuthor,
        metadataPhotoLicense,
        metadataPhotoLicenseUrl,
        metadataPhotoSourceUrl,
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
        metadataPhotoUrl = metadataPhotoUrl,
        metadataPhotoAuthor = metadataPhotoAuthor,
        metadataPhotoLicense = metadataPhotoLicense,
        metadataPhotoLicenseUrl = metadataPhotoLicenseUrl,
        metadataPhotoSourceUrl = metadataPhotoSourceUrl,
    )
}
