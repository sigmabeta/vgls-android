package com.vgleadsheets.database.dao

import com.vgleadsheets.model.alias.SongAlias
import kotlinx.coroutines.flow.Flow

interface SongAliasDataSource : OneToManyDataSource<SongAlias> {
    fun searchByName(name: String): Flow<List<SongAlias>>

    fun getAliasesForSong(songId: Long): Flow<List<SongAlias>>
}
