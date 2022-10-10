package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.dao.GameDao
import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.Flow

class GameRoomDaoWrapper(
    private val roomImpl: GameRoomDao,
    private val songRoomImpl: SongRoomDao
) : GameDao {
    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)


    override fun getOneByIdSync(id: Long): Game {
        TODO("Not yet implemented")
    }

    override suspend fun insert(models: List<Game>) {
        TODO("Not yet implemented")
    }

    override suspend fun nukeTable() {
        TODO("Not yet implemented")
    }

    override fun searchByName(name: String): Flow<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getAll(withRelated: Boolean): Flow<List<Game>> {
        TODO("Not yet implemented")
    }
}
