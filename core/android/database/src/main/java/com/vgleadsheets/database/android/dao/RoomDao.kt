package com.vgleadsheets.database.android.dao

import kotlinx.coroutines.flow.Flow

interface RoomDao<EntityType> {
    fun getOneById(id: Long): Flow<EntityType>

    fun getOneByIdSync(id: Long): EntityType

    fun getAll(): Flow<List<EntityType>>

    fun insert(entities: List<@JvmSuppressWildcards EntityType>)

    suspend fun nukeTable()

    companion object {
        const val ROW_PRIMARY_KEY_ID = "id"

        const val GET = "SELECT * FROM"
        const val DELETE = "DELETE FROM"

        const val WHERE_SINGLE = "WHERE id = :$ROW_PRIMARY_KEY_ID"
        const val WHERE_SEARCH = "WHERE name LIKE :name"

        const val OPTION_CASE_INSENSITIVE = "COLLATE NOCASE"
        const val OPTION_ALPHABETICAL_ORDER = "ORDER BY name"
    }
}
