package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface RegularDao<ModelType> : Dao<ModelType> {
    fun getAll(): Flow<List<ModelType>>
}
