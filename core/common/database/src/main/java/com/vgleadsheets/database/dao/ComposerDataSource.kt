package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.relation.SongComposerRelation
import kotlinx.coroutines.flow.Flow

interface ComposerDataSource : DataSource<Composer> {
    fun getFavorites(): Flow<List<Composer>>

    fun getMostSongsComposers(): Flow<List<Composer>>

    fun searchByName(name: String): Flow<List<Composer>>

    fun insertRelations(relations: List<SongComposerRelation>)

    fun getComposersForSong(songId: Long): Flow<List<Composer>>

    fun getComposersForSongSync(songId: Long): List<Composer>

    fun incrementSheetsPlayed(composerId: Long)

    fun toggleFavorite(composerId: Long)

    fun toggleOffline(composerId: Long)
}
