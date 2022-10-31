package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import kotlinx.coroutines.flow.Flow

interface ComposerDataSource : OneToManyDataSource<Composer> {
    fun searchByName(name: String): Flow<List<Composer>>
}
