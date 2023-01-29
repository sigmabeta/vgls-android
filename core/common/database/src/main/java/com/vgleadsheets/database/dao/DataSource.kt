package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface DataSource<ModelType> {
    fun getOneById(id: Long): Flow<ModelType>

    fun getOneByIdSync(id: Long): ModelType

    fun remove(ids: List<Long>)

    fun insert(models: List<ModelType>)

    fun nukeTable()
}
