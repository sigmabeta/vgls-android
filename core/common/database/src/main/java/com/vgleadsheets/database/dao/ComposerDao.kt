package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import kotlinx.coroutines.flow.Flow

interface ComposerDao: OneToManyDao<Composer> {
    fun searchByName(name: String): Flow<List<Composer>>
}
