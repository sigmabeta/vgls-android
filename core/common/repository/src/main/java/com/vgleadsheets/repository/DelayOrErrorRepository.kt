package com.vgleadsheets.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random

@Suppress("UnusedPrivateMember")
class DelayOrErrorRepository(
    private val realRepository: RealRepository
) : VglsRepository {
    override suspend fun checkShouldAutoUpdate() = realRepository.checkShouldAutoUpdate()

    override fun refresh() = realRepository.refresh()

    override fun refreshJamStateContinuously(name: String) =
        realRepository.refreshJamStateContinuously(name)

    override suspend fun refreshJamState(name: String) = realRepository.refreshJamState(name)

    override fun observeJamState(id: Long) = realRepository.observeJamState(id)

    override fun getAllGames(withSongs: Boolean) = realRepository.getAllGames()

    override fun getAllSongs(withComposers: Boolean) = realRepository.getAllSongs(withComposers)

    override fun getAllComposers(withSongs: Boolean) = realRepository.getAllComposers(withSongs)

    override fun getAllTagKeys(withValues: Boolean) = realRepository.getAllTagKeys(withValues)

    override fun getAllJams(withHistory: Boolean) = realRepository.getAllJams(withHistory)

    override fun getSongsForGame(gameId: Long, withComposers: Boolean) =
        realRepository.getSongsForGame(gameId)

    override fun getTagValuesForSong(songId: Long) = realRepository.getTagValuesForSong(songId)

    override fun getSongsForTagValue(tagValueId: Long) =
        realRepository.getSongsForTagValue(tagValueId)

    override fun getTagValuesForTagKey(tagKeyId: Long) =
        realRepository.getTagValuesForTagKey(tagKeyId)

    override fun getSetlistEntriesForJam(jamId: Long) =
        realRepository.getSetlistEntriesForJam(jamId)

    override fun getSongHistoryForJam(jamId: Long) = realRepository.getSongHistoryForJam(jamId)

    override fun getAliasesForSong(songId: Long) = realRepository.getAliasesForSong(songId)

    override fun getSong(songId: Long) = realRepository.getSong(songId)

    override fun getComposer(composerId: Long) = realRepository.getComposer(composerId)

    override fun getGame(gameId: Long) = realRepository.getGame(gameId)

    override fun getTagKey(tagKeyId: Long) = realRepository.getTagKey(tagKeyId)

    override fun getTagValue(tagValueId: Long) = realRepository.getTagValue(tagValueId)

    override fun getLastUpdateTime() = realRepository.getLastUpdateTime()

    override fun getJam(id: Long, withHistory: Boolean) = realRepository.getJam(id, withHistory)

    override fun searchSongsCombined(searchQuery: String) =
        realRepository.searchSongsCombined(searchQuery)

    override fun searchGamesCombined(searchQuery: String) =
        realRepository.searchGamesCombined(searchQuery)

    override fun searchComposersCombined(searchQuery: String) =
        realRepository.searchComposersCombined(searchQuery)

    override suspend fun removeJam(id: Long) = realRepository.removeJam(id)

    override suspend fun refreshJams() = realRepository.refreshJams()

    override fun clearSheets() = realRepository.clearSheets()

    override suspend fun clearJams() = realRepository.clearJams()

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
