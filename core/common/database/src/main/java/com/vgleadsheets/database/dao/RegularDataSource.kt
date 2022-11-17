package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface RegularDataSource<ModelType> : DataSource<ModelType> {
    fun getAll(): Flow<List<ModelType>>
}
