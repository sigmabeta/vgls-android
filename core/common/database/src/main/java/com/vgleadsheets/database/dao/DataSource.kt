package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface DataSource<ModelType> {
    fun getOneById(id: Long): Flow<ModelType>

    fun getOneByIdSync(id: Long): ModelType

    suspend fun insert(models: List<ModelType>)

    suspend fun nukeTable()
}
