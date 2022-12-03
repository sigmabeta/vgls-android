package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface OneToOneDataSource<ModelType> : DataSource<ModelType> {
    fun getAll(): Flow<List<ModelType>>
}
