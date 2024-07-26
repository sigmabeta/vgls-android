package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.Composer
import kotlinx.coroutines.flow.Flow

class ComposerRepository(
    private val composerDataSource: ComposerDataSource,
) {
    fun getAllComposers() = composerDataSource
        .getAll()

    fun getFavoriteComposers() =
        composerDataSource.getFavorites()

    fun getComposersForSong(songId: Long) = composerDataSource
        .getComposersForSong(songId)

    suspend fun getComposersForSongSync(composerId: Long) = composerDataSource
        .getComposersForSongSync(composerId)

    fun getComposer(composerId: Long): Flow<Composer> = composerDataSource
        .getOneById(composerId)

    fun getMostSongsComposers() = composerDataSource
        .getMostSongsComposers()
}
