package com.vgleadsheets.repository

import com.vgleadsheets.conversion.asModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.network.FakeModelGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeRepository(
    private val fakeModelGenerator: FakeModelGenerator,
    private val dispatchers: VglsDispatchers,
) : VglsRepository {
    override suspend fun checkShouldAutoUpdate() = false

    override fun refresh() = flow { emit(Unit) }
    override fun getAllGames() = flow {
        val games = fakeModelGenerator.possibleGames
            ?.map {
                it.asModel(
                    0,
                    isFavorite = false,
                    isAvailableOffline = false
                )
            }
            .orEmpty()

        emit(games)
    }.flowOn(dispatchers.disk)

    override fun getAllSongs() = flow {
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

    override fun getAllComposers() = flow {
        val composers = fakeModelGenerator.possibleComposers
            ?.map { apiComposer ->
                apiComposer.asModel(
                    sheetsPlayed = 0,
                    isFavorite = false,
                    isAvailableOffline = false,
                )
            }
            .orEmpty()

        emit(composers)
    }.flowOn(dispatchers.disk)

    override fun getAllTagKeys(): Flow<List<TagKey>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteGames(): Flow<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteSongs(): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteComposers(): Flow<List<Composer>> {
        TODO("Not yet implemented")
    }

    override fun getSongsForGame(gameId: Long): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun getSongsForGameSync(gameId: Long): List<Song> {
        TODO("Not yet implemented")
    }

    override fun getSongsForComposer(composerId: Long): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun getComposersForSong(composerId: Long): Flow<List<Composer>> {
        TODO("Not yet implemented")
    }

    override fun getComposersForSongSync(composerId: Long): List<Composer> {
        TODO("Not yet implemented")
    }

    override fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>> {
        TODO("Not yet implemented")
    }

    override fun getTagValuesForSong(songId: Long): Flow<List<TagValue>> {
        TODO("Not yet implemented")
    }

    override fun getAliasesForSong(songId: Long): Flow<List<SongAlias>> {
        TODO("Not yet implemented")
    }

    override fun getSong(songId: Long): Flow<Song> {
        TODO("Not yet implemented")
    }

    override fun getComposer(composerId: Long): Flow<Composer> {
        TODO("Not yet implemented")
    }

    override fun getGame(gameId: Long): Flow<Game> {
        TODO("Not yet implemented")
    }

    override fun getGameSync(gameId: Long): Game {
        TODO("Not yet implemented")
    }

    override fun getTagKey(tagKeyId: Long): Flow<TagKey> {
        TODO("Not yet implemented")
    }

    override fun getTagValue(tagValueId: Long): Flow<TagValue> {
        TODO("Not yet implemented")
    }

    override fun getLastUpdateTime(): Flow<Time> {
        TODO("Not yet implemented")
    }

    override fun searchSongsCombined(searchQuery: String): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun searchGamesCombined(searchQuery: String): Flow<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun searchComposersCombined(searchQuery: String): Flow<List<Composer>> {
        TODO("Not yet implemented")
    }

    override suspend fun incrementViewCounter(songId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteSong(songId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteGame(gameId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteComposer(composerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleOfflineSong(songId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleOfflineGame(gameId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleOfflineComposer(composerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleAlternate(songId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun clearSheets() {
        TODO("Not yet implemented")
    }
}
