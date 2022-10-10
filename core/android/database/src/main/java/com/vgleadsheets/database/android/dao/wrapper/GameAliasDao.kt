package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.alias.GameAlias
import kotlinx.coroutines.flow.Flow

class GameAliasRoomDaoWrapper(
    private val roomImpl: RoomDao
) : Dao {

    override fun getAliasesByName(name: String): Flow<List<GameAlias>>

    override suspend fun insertAll(aliasEntities: List<GameAlias>)

    override suspend fun nukeTable()
}
