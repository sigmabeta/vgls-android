package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface ModelDao<ModelType>: Dao<ModelType> {
    fun getAll(): Flow<List<ModelType>>
}
