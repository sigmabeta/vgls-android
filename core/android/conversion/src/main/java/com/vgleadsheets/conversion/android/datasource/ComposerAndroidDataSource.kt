package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.ManyToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.SongsForComposerDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.relation.SongComposerRelation

class ComposerAndroidDataSource(
    private val convert: ComposerConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: ComposerRoomDao,
    private val relatedRoomImpl: SongsForComposerDao
) : ManyToManyAndroidDataSource<
    ComposerRoomDao,
    Composer,
    ComposerEntity,
    Song,
    SongEntity,
    ComposerRoomDao,
    SongsForComposerDao,
    ComposerConverter,
    SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
),
    ComposerDataSource {
    override fun getFavorites() = roomImpl
        .getFavorites()
        .mapList { convert.entityToModelWithMany(it, relatedRoomImpl, manyConverter) }

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModelWithMany(it, relatedRoomImpl, manyConverter) }

    override fun insertRelations(relations: List<SongComposerRelation>) = roomImpl
        .insertJoins(
            relations.map {
                SongComposerJoin(
                    it.songId,
                    it.composerId
                )
            }
        )

    override fun getSongsForComposer(composerId: Long) = relatedRoomImpl
        .getJoinedEntities(composerId)
        .mapList {
            manyConverter.entityToModel(it)
        }

    override fun incrementSheetsPlayed(composerId: Long) = roomImpl.incrementSheetsPlayed(composerId)

    override fun toggleFavorite(composerId: Long) = roomImpl.toggleFavorite(composerId)

    override fun toggleOffline(composerId: Long) = roomImpl.toggleOffline(composerId)
}
