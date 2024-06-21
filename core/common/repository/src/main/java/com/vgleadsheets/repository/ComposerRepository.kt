package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.Composer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ComposerRepository(
    private val dispatchers: VglsDispatchers,
    private val composerAliasDataSource: ComposerAliasDataSource,
    private val composerDataSource: ComposerDataSource,
) {
    fun getAllComposers() = composerDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    fun getFavoriteComposers() =
        composerDataSource.getFavorites().flowOn(dispatchers.disk)

    fun getComposersForSong(songId: Long) = composerDataSource
        .getComposersForSong(songId)
        .flowOn(dispatchers.disk)

    fun getComposersForSongSync(composerId: Long) = composerDataSource
        .getComposersForSongSync(composerId)

    fun getComposer(composerId: Long): Flow<Composer> = composerDataSource
        .getOneById(composerId)
        .flowOn(dispatchers.disk)

    fun searchComposersCombined(searchQuery: String) = combine(
        searchComposers(searchQuery),
        searchComposerAliases(searchQuery)
    ) { composers: List<Composer>, composerAliases: List<Composer> ->
        composers + composerAliases
    }.map { composers ->
        composers.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    fun getMostSongsComposers() = composerDataSource
        .getMostSongsComposers()
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull { it.composer }
        }
}
