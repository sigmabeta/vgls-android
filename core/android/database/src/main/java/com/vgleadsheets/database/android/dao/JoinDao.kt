package com.vgleadsheets.database.android.dao

import kotlinx.coroutines.flow.Flow

interface JoinDao<EntityType> {
    fun getJoinedEntities(id: Long): Flow<List<EntityType>>

    fun getJoinedEntitiesSync(id: Long): List<EntityType>
}
