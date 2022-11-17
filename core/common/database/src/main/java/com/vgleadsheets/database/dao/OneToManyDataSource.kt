package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface OneToManyDataSource<ModelType> : DataSource<ModelType> {
    fun getAll(withRelated: Boolean): Flow<List<ModelType>>
}
