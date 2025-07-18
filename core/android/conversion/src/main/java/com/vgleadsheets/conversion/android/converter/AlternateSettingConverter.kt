package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.entity.FavoriteSongEntity
import com.vgleadsheets.model.history.Favorite

class AlternateSettingConverter : Converter<Favorite, FavoriteSongEntity> {
    override fun Favorite.toEntity() = FavoriteSongEntity(id)

    override fun FavoriteSongEntity.toModel() = Favorite(id)
}
