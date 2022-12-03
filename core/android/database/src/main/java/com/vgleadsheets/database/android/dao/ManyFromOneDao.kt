package com.vgleadsheets.database.android.dao

import kotlinx.coroutines.flow.Flow

interface ManyFromOneDao<EntityType> : RoomDao<EntityType> {
    fun getEntitiesForForeign(id: Long): Flow<List<EntityType>>

    fun getEntitiesForForeignSync(id: Long): List<EntityType>
}
