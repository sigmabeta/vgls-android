package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface DataSource<ModelType> {
    fun getOneById(id: Long): Flow<ModelType>

    suspend fun getOneByIdSync(id: Long): ModelType

    fun getAll(): Flow<List<ModelType>>

    suspend fun remove(ids: List<Long>)

    suspend fun insert(models: List<ModelType>)

    suspend fun nukeTable()
}
