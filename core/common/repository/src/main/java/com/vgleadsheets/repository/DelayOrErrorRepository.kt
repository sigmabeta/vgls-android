package com.vgleadsheets.repository

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@Suppress("UnusedPrivateMember")
class DelayOrErrorRepository(
    private val realRepository: RealRepository
) : VglsRepository {
    override suspend fun checkShouldAutoUpdate() = realRepository.checkShouldAutoUpdate()

    override fun refresh() = realRepository.refresh()

    override fun getAllGames(withSongs: Boolean) = realRepository.getAllGames()

    override fun getAllSongs(withComposers: Boolean) = realRepository.getAllSongs(withComposers)

    override fun getAllComposers(withSongs: Boolean) = realRepository.getAllComposers(withSongs)

    override fun getAllTagKeys(withValues: Boolean) = realRepository.getAllTagKeys(withValues)

    override fun getFavoriteGames(withSongs: Boolean) = realRepository.getFavoriteGames(withSongs)

    override fun getFavoriteSongs(withComposers: Boolean) =
        realRepository.getFavoriteSongs(withComposers)

    override fun getFavoriteComposers(withSongs: Boolean) =
        realRepository.getFavoriteComposers(withSongs)

    override fun getSongsForGame(gameId: Long, withComposers: Boolean) =
        realRepository.getSongsForGame(gameId)

    override fun getTagValuesForSong(songId: Long) = realRepository.getTagValuesForSong(songId)

    override fun getSongsForTagValue(tagValueId: Long) =
        realRepository.getSongsForTagValue(tagValueId)

    override fun getTagValuesForTagKey(tagKeyId: Long) =
        realRepository.getTagValuesForTagKey(tagKeyId)

    override fun getAliasesForSong(songId: Long) = realRepository.getAliasesForSong(songId)

    override fun getSong(songId: Long) = realRepository.getSong(songId)

    override fun getComposer(composerId: Long) = realRepository.getComposer(composerId)

    override fun getGame(gameId: Long) = realRepository.getGame(gameId)

    override fun getTagKey(tagKeyId: Long) = realRepository.getTagKey(tagKeyId)

    override fun getTagValue(tagValueId: Long) = realRepository.getTagValue(tagValueId)

    override fun getLastUpdateTime() = realRepository.getLastUpdateTime()

    override fun searchSongsCombined(searchQuery: String) =
        realRepository.searchSongsCombined(searchQuery)

    override fun searchGamesCombined(searchQuery: String) =
        realRepository.searchGamesCombined(searchQuery)

    override fun searchComposersCombined(searchQuery: String) =
        realRepository.searchComposersCombined(searchQuery)

    override suspend fun incrementViewCounter(songId: Long) =
        realRepository.incrementViewCounter(songId)

    override suspend fun toggleFavoriteSong(songId: Long) =
        realRepository.toggleFavoriteSong(songId)

    override suspend fun toggleFavoriteGame(gameId: Long) =
        realRepository.toggleFavoriteGame(gameId)

    override suspend fun toggleFavoriteComposer(composerId: Long) =
        realRepository.toggleFavoriteComposer(composerId)

    override suspend fun toggleOfflineSong(songId: Long) = realRepository.toggleOfflineSong(songId)

    override suspend fun toggleOfflineGame(gameId: Long) = realRepository.toggleOfflineGame(gameId)

    override suspend fun toggleOfflineComposer(composerId: Long) =
        realRepository.toggleOfflineComposer(composerId)

    override suspend fun toggleAlternate(songId: Long) = realRepository.toggleAlternate(songId)

    override suspend fun clearSheets() = realRepository.clearSheets()

    private suspend fun <EventType, FlowType : Flow<EventType>> FlowType.butItTakesForever() =
        onEach {
            delay(
                DELAY_MINIMUM_MS + Random.nextLong(DELAY_VARIANCE_MS),
            )
        }

    private suspend fun <EventType, FlowType : Flow<List<EventType>>> FlowType.butItsAlwaysEmpty() =
        onStart {
            emit(emptyList())
        }.first()

    private fun <EventType, FlowType : Flow<EventType>> FlowType.butItFailsEveryTime() = this.map {
        if (SHOULD_IT_FAIL) {
            error(BUT_IT_FAILS_EVERY_TIME)
        } else {
            it
        }
    }

    companion object {
        const val DELAY_MINIMUM_MS = 1_000L

        const val DELAY_VARIANCE_MS = 4_000L

        const val SHOULD_IT_FAIL = true

        const val BUT_IT_FAILS_EVERY_TIME =
            "This repository request is configured to fail every time."
    }
}
