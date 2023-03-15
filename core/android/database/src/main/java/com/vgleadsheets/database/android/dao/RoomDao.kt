package com.vgleadsheets.database.android.dao

import com.vgleadsheets.database.android.enitity.DeletionId
import kotlinx.coroutines.flow.Flow

interface RoomDao<EntityType> {
    fun getOneById(id: Long): Flow<EntityType>

    fun getOneByIdSync(id: Long): EntityType

    fun getAll(): Flow<List<EntityType>>

    fun insert(entities: List<@JvmSuppressWildcards EntityType>)

    fun remove(ids: List<DeletionId>)

    fun nukeTable()

    companion object {
        const val ROW_PRIMARY_KEY_ID = "id"

        const val GET = "SELECT * FROM"
        const val DELETE = "DELETE FROM"
        const val UPDATE = "UPDATE"
        const val SET = "SET"
        const val WHERE_SINGLE = "WHERE id = :$ROW_PRIMARY_KEY_ID"
        const val WHERE_SEARCH = "WHERE name LIKE :name"

        const val OPTION_CASE_INSENSITIVE = "COLLATE NOCASE"
        const val OPTION_ALPHABETICAL_ORDER = "ORDER BY name"

        const val COLUMN_FAVORITE = "isFavorite"
        const val COLUMN_OFFLINE = "isAvailableOffline"

        const val TOGGLE_FAVORITE = "$SET $COLUMN_FAVORITE = (1 - $COLUMN_FAVORITE)"
        const val TOGGLE_OFFLINE = "$SET $COLUMN_OFFLINE = (1 - $COLUMN_OFFLINE)"
    }
}
