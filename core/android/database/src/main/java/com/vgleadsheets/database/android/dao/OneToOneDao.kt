package com.vgleadsheets.database.android.dao

import kotlinx.coroutines.flow.Flow

interface OneToOneDao<EntityType> : RoomDao<EntityType> {
    fun getForeignEntity(id: Long): Flow<EntityType>

    fun getForeignEntitySync(id: Long): EntityType
}
