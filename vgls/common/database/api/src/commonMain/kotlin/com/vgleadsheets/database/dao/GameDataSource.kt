package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.Flow

interface GameDataSource : DataSource<Game> {
    fun getFavorites(): Flow<List<Game>>

    fun getMostSongsGames(): Flow<List<Game>>

    fun getByIdList(ids: List<Long>): Flow<List<Game>>

    fun searchByName(name: String): Flow<List<Game>>

    suspend fun incrementSheetsPlayed(gameId: Long)

    suspend fun toggleFavorite(gameId: Long)

    suspend fun toggleOffline(gameId: Long)

    fun getHighestId(): Flow<Long>

    /**
     * Replaces every game's IGDB cover image id with [imageIdByGameId]: games in it get that id,
     * games not in it lose theirs. Callers run it inside a transaction.
     */
    suspend fun replaceIgdbImageIds(imageIdByGameId: Map<Long, String>)

    /**
     * Whether any game currently has a cover. False after the table was cleared (or before the
     * first cover pull), which the updater treats as "the stored mapping is out of sync with the
     * database, fetch again" rather than trusting the ETag.
     */
    suspend fun hasIgdbImage(): Boolean
}
