package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.relation.SongComposerRelation

class ComposerAndroidDataSource(
    private val convert: ComposerConverter,
    private val roomImpl: ComposerRoomDao,
) : AndroidDataSource<
    ComposerRoomDao,
    Composer,
    ComposerEntity,
    ComposerConverter>(
    convert,
    roomImpl,
),
    ComposerDataSource {
    override fun getFavorites() = roomImpl
        .getFavorites()
        .mapList { convert.entityToModel(it) }

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }

    override fun insertRelations(relations: List<SongComposerRelation>) = roomImpl
        .insertJoins(
            relations.map {
                SongComposerJoin(
                    it.songId,
                    it.composerId
                )
            }
        )

    override fun getComposersForSong(songId: Long) = roomImpl
        .getForSong(songId)
        .mapList { convert.entityToModel(it) }

    override fun incrementSheetsPlayed(composerId: Long) = roomImpl.incrementSheetsPlayed(composerId)

    override fun toggleFavorite(composerId: Long) = roomImpl.toggleFavorite(composerId)

    override fun toggleOffline(composerId: Long) = roomImpl.toggleOffline(composerId)
}
