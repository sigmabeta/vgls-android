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
        const val COLUMN_PRIMARY_KEY_ID = "id"

        const val GET = "SELECT * FROM"
        const val DELETE = "DELETE FROM"
        const val UPDATE = "UPDATE"
        const val SET = "SET"
        const val DROP = "DROP TABLE"
        const val WHERE = "WHERE"
        const val INNER_JOIN = "INNER JOIN"
        const val ON = "ON"

        const val WHERE_SINGLE = "$WHERE id = :$COLUMN_PRIMARY_KEY_ID"
        const val WHERE_FAVORITE = "$WHERE isFavorite = 1"
        const val WHERE_SEARCH = "$WHERE name LIKE :name"

        const val OPTION_CASE_INSENSITIVE = "COLLATE NOCASE"
        const val OPTION_ALPHABETICAL_ORDER = "ORDER BY name"
        const val OPTION_SONG_COUNT_ORDER = "ORDER BY songCount DESC"
        const val OPTION_LIMIT = "LIMIT"

        const val COLUMN_FAVORITE = "isFavorite"
        const val COLUMN_OFFLINE = "isAvailableOffline"

        const val TOGGLE_FAVORITE = "$SET $COLUMN_FAVORITE = (1 - $COLUMN_FAVORITE)"
        const val TOGGLE_OFFLINE = "$SET $COLUMN_OFFLINE = (1 - $COLUMN_OFFLINE)"
    }
}
