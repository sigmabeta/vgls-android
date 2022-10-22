package com.vgleadsheets.database.android.dao

import kotlinx.coroutines.flow.Flow

interface ManyToManyDao<EntityType, JoinType> : RoomDao<EntityType> {
    suspend fun insertJoins(joins: List<JoinType>)

    fun getJoinedEntities(id: Long): Flow<List<EntityType>>

    fun getJoinedEntitiesSync(id: Long): List<EntityType>
}
