package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.SearchHistoryDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SearchHistoryEntry
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SearchRepository(
    private val searchHistoryDataSource: SearchHistoryDataSource,
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
    private val gameDataSource: GameDataSource,
    private val gameAliasDataSource: GameAliasDataSource,
    private val composerDataSource: ComposerDataSource,
    private val composerAliasDataSource: ComposerAliasDataSource,
) {
    fun getRecentSearches() = searchHistoryDataSource
        .getRecentEntries()

    suspend fun addToSearchHistory(query: String) = searchHistoryDataSource.add(
        SearchHistoryEntry(
            null,
            query,
            System.currentTimeMillis()
        )
    )

    suspend fun removeFromSearchHistory(id: Long) = searchHistoryDataSource.removeEntry(id)

    fun searchSongsCombined(searchQuery: String) = combine(
        searchSongs(searchQuery),
        searchSongAliases(searchQuery)
    ) { songs: List<Song>, songAliases: List<Song> ->
        songs + songAliases
    }.map { songs ->
        songs.distinctBy { it.id }
    }

    fun searchGamesCombined(searchQuery: String) = combine(
        searchGames(searchQuery),
        searchGameAliases(searchQuery)
    ) { games: List<Game>, gameAliases: List<Game> ->
        games + gameAliases
    }.map { games ->
        games.distinctBy { it.id }
    }

    fun searchComposersCombined(searchQuery: String) = combine(
        searchComposers(searchQuery),
        searchComposerAliases(searchQuery)
    ) { composers: List<Composer>, composerAliases: List<Composer> ->
        composers + composerAliases
    }.map { composers ->
        composers.distinctBy { it.id }
    }

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchSongAliases(searchQuery: String) = songAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .mapListTo {
            val tempSong = songDataSource.getOneByIdSync(it.songId)
            tempSong.copy(
                name = "${tempSong.name} (${it.name})"
            )
        }

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String) = gameDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchGameAliases(searchQuery: String) = gameAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .mapListTo {
            val tempGame = gameDataSource.getOneByIdSync(it.gameId)
            tempGame.copy(
                name = "${tempGame.name} (${it.name})"
            )
        }

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .mapListTo {
            val tempComposer = composerDataSource.getOneByIdSync(it.composerId)
            tempComposer.copy(
                name = "${tempComposer.name} (${it.name})"
            )
        }
}
