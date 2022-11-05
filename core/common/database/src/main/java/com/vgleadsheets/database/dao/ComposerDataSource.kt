package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.relation.SongComposerRelation
import kotlinx.coroutines.flow.Flow

interface ComposerDataSource : OneToManyDataSource<Composer> {
    fun searchByName(name: String): Flow<List<Composer>>

    suspend fun insertRelations(relations: List<SongComposerRelation>)
}
