package com.vgleadsheets.repository

import com.vgleadsheets.model.song.Song
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class DelayOrErrorRepository(
    val realRepository: RealRepository
) : Repository {
    override fun checkShouldAutoUpdate() = realRepository.checkShouldAutoUpdate()

    override fun refresh() = realRepository.refresh()

    override fun refreshJamStateContinuously(name: String) =
        realRepository.refreshJamStateContinuously(name)

    override fun refreshJamState(name: String) = realRepository.refreshJamState(name)

    override fun refreshSetlist(jamId: Long, name: String) =
        realRepository.refreshSetlist(jamId, name)

    override fun observeJamState(id: Long) = realRepository.observeJamState(id)

    override fun getGames(withSongs: Boolean) = realRepository.getGames()

    override fun getAllSongs(withComposers: Boolean) = realRepository.getAllSongs(withComposers)

    override fun getComposers(withSongs: Boolean) = realRepository.getComposers(withSongs)

    override fun getAllTagKeys(withValues: Boolean) = realRepository.getAllTagKeys(withValues)

    override fun getJams() = realRepository.getJams()

    override fun getTagValuesForTagKey(
        tagKeyId: Long,
        withSongs: Boolean
    ) = realRepository.getTagValuesForTagKey(tagKeyId, withSongs)

    override fun getSongsByComposer(composerId: Long) =
        realRepository.getSongsByComposer(composerId)

    override fun getSongsForTagValue(tagValueId: Long) =
        realRepository.getSongsForTagValue(tagValueId)

    override fun getTagValuesForSong(songId: Long) = realRepository.getTagValuesForSong(songId)

    override fun getSetlistForJam(jamId: Long) = realRepository.getSetlistForJam(jamId)

    override fun getSongsForGame(gameId: Long, withComposers: Boolean) =
        realRepository.getSongsForGame(gameId, withComposers)

    override fun getSong(songId: Long, withComposers: Boolean) =
        realRepository.getSong(songId, withComposers)

    override fun getComposer(composerId: Long) = realRepository.getComposer(composerId)

    override fun getGame(gameId: Long) = realRepository.getGame(gameId)

    override fun getTagKey(tagKeyId: Long) = realRepository.getTagKey(tagKeyId)

    override fun getTagValue(tagValueId: Long) = realRepository.getTagValue(tagValueId)

    override fun getLastUpdateTime() = realRepository.getLastUpdateTime()

    override fun getJam(id: Long, withHistory: Boolean) = realRepository.getJam(id, withHistory)

    override fun searchSongs(searchQuery: String): Observable<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun searchGamesCombined(searchQuery: String) =
        realRepository.searchGamesCombined(searchQuery)

    override fun searchComposersCombined(searchQuery: String) =
        realRepository.searchComposersCombined(searchQuery)

    override fun removeJam(id: Long) = realRepository.removeJam(id)

    override fun clearSheets() = realRepository.clearSheets()

    override fun clearJams() = realRepository.clearJams()

    private fun <EventType, RxType : Observable<EventType>> RxType.withDelay() =
        delay(DELAY_MS, TimeUnit.MILLISECONDS)

    companion object {
        const val DELAY_MS = 3_000L
    }
}
