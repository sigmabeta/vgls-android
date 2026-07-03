package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.FavoriteComposerEntity
import com.vgleadsheets.model.history.Favorite

class FavoriteComposerConverter : Converter<Favorite, FavoriteComposerEntity> {
    override fun Favorite.toEntity() = FavoriteComposerEntity(id)

    override fun FavoriteComposerEntity.toModel() = Favorite(id)
}
