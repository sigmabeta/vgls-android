package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.database.android.dao.ComposerGameCover
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.IgdbImages
import com.vgleadsheets.model.relation.SongComposerRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ComposerAndroidDataSource(
    private val convert: ComposerConverter,
    private val roomImpl: ComposerRoomDao,
) : AndroidDataSource<
    ComposerRoomDao,
    Composer,
    ComposerEntity,
    ComposerConverter
    >(
    convert,
    roomImpl,
),
    ComposerDataSource {
    override fun getOneById(id: Long) = combine(
        roomImpl.getOneById(id),
        gameImageUrlsByComposer(),
    ) { entity, gameImageUrls -> entity.toModel(gameImageUrls) }

    override suspend fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .toModel(gameImageUrlsByComposerSync())

    override fun getAll() = roomImpl
        .getAll()
        .toModelsWithGameImages()

    override fun getMostSongsComposers() = roomImpl
        .getMostSongsComposers()
        .toModelsWithGameImages()

    override fun getByIdList(ids: List<Long>) = roomImpl
        .getByIdList(ids.toTypedArray())
        .toModelsWithGameImages()

    override fun getFavorites() = roomImpl
        .getFavorites()
        .toModelsWithGameImages()

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .toModelsWithGameImages()

    override suspend fun insertRelations(relations: List<SongComposerRelation>) = roomImpl
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
        .toModelsWithGameImages()

    override suspend fun getComposersForSongSync(songId: Long): List<Composer> {
        val gameImageUrls = gameImageUrlsByComposerSync()
        return roomImpl
            .getForSongSync(songId)
            .map { it.toModel(gameImageUrls) }
    }

    override suspend fun incrementSheetsPlayed(composerId: Long) = roomImpl.incrementSheetsPlayed(composerId)

    override suspend fun toggleFavorite(composerId: Long) = roomImpl.toggleFavorite(composerId)

    override suspend fun toggleOffline(composerId: Long) = roomImpl.toggleOffline(composerId)

    override fun getHighestId() = roomImpl
        .getHighestId()
        .map { it.id }

    private fun Flow<List<ComposerEntity>>.toModelsWithGameImages() = combine(
        this,
        gameImageUrlsByComposer(),
    ) { entities, gameImageUrls -> entities.map { it.toModel(gameImageUrls) } }

    private fun ComposerEntity.toModel(gameImageUrls: Map<Long, List<String>>) = convert
        .entityToModel(this)
        .copy(gameImageUrls = gameImageUrls[id].orEmpty())

    private fun gameImageUrlsByComposer() = roomImpl
        .getGameCovers()
        .map { it.toImageUrlsByComposer() }

    private suspend fun gameImageUrlsByComposerSync() = roomImpl
        .getGameCoversSync()
        .toImageUrlsByComposer()

    private fun List<ComposerGameCover>.toImageUrlsByComposer() = groupBy(
        keySelector = { it.composerId },
        valueTransform = { it.photoUrl ?: it.igdbImageId?.let(IgdbImages::coverUrl) },
    ).mapValues { (_, urls) -> urls.filterNotNull().take(MAX_GAME_IMAGES) }

    companion object {
        // A composer's image is a collage of at most 4 game covers (a 2x2 grid); more go unused.
        private const val MAX_GAME_IMAGES = 4
    }
}
