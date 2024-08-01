package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.FavoriteGameEntity
import com.vgleadsheets.model.history.Favorite

class FavoriteGameConverter : Converter<Favorite, FavoriteGameEntity> {
    override fun Favorite.toEntity() = FavoriteGameEntity(id)

    override fun FavoriteGameEntity.toModel() = Favorite(id)
}
