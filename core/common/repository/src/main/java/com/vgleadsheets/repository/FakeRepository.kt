package com.vgleadsheets.repository

import com.vgleadsheets.conversion.asModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeRepository(
    private val fakeModelGenerator: FakeModelGenerator,
    private val dispatchers: VglsDispatchers,
) : VglsRepository {
    fun getAllGames() = flow {
        val games = fakeModelGenerator.possibleGames
            ?.map {
                it.asModel(
                    0,
                    isFavorite = false,
                    isAvailableOffline = false,
                    hasVocalSongs = false,
                    songCount = 0
                )
            }
            .orEmpty()

        emit(games)
    }.flowOn(dispatchers.disk)

    fun getAllSongs() = flow {
        val songs = fakeModelGenerator.possibleGames
            ?.map { apiGame ->
                apiGame.songs.map { apiSong ->
                    apiSong.asModel(
                        apiGame.game_id,
                        apiGame.game_name,
                        playCount = 0,
                        isFavorite = false,
                        isAvailableOffline = false,
                        isAltSelected = false,
                    )
                }
            }
            .orEmpty()
            .flatten()

        emit(songs)
    }.flowOn(dispatchers.disk)

    override fun getAllTagKeys(): Flow<List<TagKey>> {
        TODO("Not yet implemented")
    }

    override fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>> {
        TODO("Not yet implemented")
    }

    override fun getTagValuesForSong(songId: Long): Flow<List<TagValue>> {
        TODO("Not yet implemented")
    }

    override fun getTagKey(tagKeyId: Long): Flow<TagKey> {
        TODO("Not yet implemented")
    }

    override fun getTagValue(tagValueId: Long): Flow<TagValue> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteGame(gameId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteComposer(composerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleOfflineGame(gameId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleOfflineComposer(composerId: Long) {
        TODO("Not yet implemented")
    }
}
