package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Suppress("TooGenericExceptionCaught", "PrintStackTrace")
class RealRepository(
    private val dispatchers: VglsDispatchers,
    private val gameDataSource: GameDataSource,
    private val composerAliasDataSource: ComposerAliasDataSource,
    private val composerDataSource: ComposerDataSource,
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
    private val tagKeyDataSource: TagKeyDataSource,
    private val tagValueDataSource: TagValueDataSource
) : VglsRepository {

    override fun getAllSongs() = songDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    override fun getAllComposers() = composerDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    override fun getFavoriteSongs() =
        songDataSource.getFavorites().flowOn(dispatchers.disk)

    override fun getFavoriteComposers() =
        composerDataSource.getFavorites().flowOn(dispatchers.disk)

    override fun getAllTagKeys() = tagKeyDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    override fun getSongsForGame(gameId: Long) = songDataSource
        .getSongsForGame(gameId)
        .flowOn(dispatchers.disk)

    override fun getSongsForGameSync(gameId: Long) = songDataSource
        .getSongsForGameSync(gameId)

    override fun getSongsForComposer(composerId: Long) = songDataSource
        .getSongsForComposer(composerId)
        .flowOn(dispatchers.disk)

    override fun getSongsForTagValue(tagValueId: Long) = songDataSource
        .getSongsForTagValue(tagValueId)
        .flowOn(dispatchers.disk)

    override fun getTagValuesForTagKey(tagKeyId: Long) = tagValueDataSource
        .getTagValuesForTagKey(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValuesForSong(songId: Long) = tagValueDataSource
        .getTagValuesForSong(songId)
        .flowOn(dispatchers.disk)

    override fun getComposersForSong(songId: Long) = composerDataSource
        .getComposersForSong(songId)
        .flowOn(dispatchers.disk)

    override fun getComposersForSongSync(composerId: Long) = composerDataSource
        .getComposersForSongSync(composerId)

    override fun getAliasesForSong(songId: Long) = songAliasDataSource
        .getAliasesForSong(songId)
        .flowOn(dispatchers.disk)

    override fun getSong(
        songId: Long
    ) = songDataSource
        .getOneById(songId)
        .flowOn(dispatchers.disk)

    override fun getComposer(composerId: Long): Flow<Composer> = composerDataSource
        .getOneById(composerId)
        .flowOn(dispatchers.disk)

    override fun getTagKey(tagKeyId: Long) = tagKeyDataSource
        .getOneById(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValue(tagValueId: Long) = tagValueDataSource
        .getOneById(tagValueId)
        .flowOn(dispatchers.disk)

    override fun searchComposersCombined(searchQuery: String) = combine(
        searchComposers(searchQuery),
        searchComposerAliases(searchQuery)
    ) { composers: List<Composer>, composerAliases: List<Composer> ->
        composers + composerAliases
    }.map { composers ->
        composers.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override fun searchSongsCombined(searchQuery: String) = combine(
        searchSongs(searchQuery),
        searchSongAliases(searchQuery)
    ) { songs: List<Song>, songAliases: List<Song> ->
        songs + songAliases
    }.map { songs ->
        songs.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override suspend fun incrementViewCounter(songId: Long) {
        val song = songDataSource.getOneByIdSync(songId)

        songDataSource.incrementPlayCount(song.id)
        gameDataSource.incrementSheetsPlayed(song.gameId)

        song.composers?.forEach {
            composerDataSource.incrementSheetsPlayed(it.id)
        }
    }

    override suspend fun toggleFavoriteSong(songId: Long) {
        songDataSource.toggleFavorite(songId)
    }

    override suspend fun toggleFavoriteGame(gameId: Long) {
        gameDataSource.toggleFavorite(gameId)
    }

    override suspend fun toggleFavoriteComposer(composerId: Long) {
        composerDataSource.toggleFavorite(composerId)
    }

    override suspend fun toggleOfflineSong(songId: Long) {
        songDataSource.toggleOffline(songId)
    }

    override suspend fun toggleOfflineGame(gameId: Long) {
        gameDataSource.toggleOffline(gameId)
    }

    override suspend fun toggleOfflineComposer(composerId: Long) {
        composerDataSource.toggleOffline(composerId)
    }

    override suspend fun toggleAlternate(songId: Long) {
        songDataSource.toggleAlternate(songId)
    }

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.composer?.copy(
                    name = it.name,
                    songs = songDataSource
                        .getSongsForComposer(it.composer!!.id)
                        .first()
                )
            }
        }

    @Suppress("MaxLineLength")
    private fun searchSongAliases(searchQuery: String) = songAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.song?.copy(
                    name = it.name,
                )
            }
        }

    companion object {
        val AGE_THRESHOLD = 4.toDuration(DurationUnit.HOURS)
    }
}
