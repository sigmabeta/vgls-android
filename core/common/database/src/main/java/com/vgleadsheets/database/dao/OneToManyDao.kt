package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface OneToManyDao<ModelType>: Dao<ModelType> {
    fun getAll(withRelated: Boolean): Flow<List<ModelType>>
}
