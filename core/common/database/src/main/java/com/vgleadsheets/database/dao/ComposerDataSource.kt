package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.relation.SongComposerRelation
import kotlinx.coroutines.flow.Flow

interface ComposerDataSource : OneToManyDataSource<Composer> {
    fun getFavorites(): Flow<List<Composer>>

    fun searchByName(name: String): Flow<List<Composer>>

    fun insertRelations(relations: List<SongComposerRelation>)

    fun getSongsForComposer(composerId: Long): Flow<List<Song>>

    fun incrementSheetsPlayed(composerId: Long)

    fun toggleFavorite(composerId: Long)

    fun toggleOffline(composerId: Long)
}
