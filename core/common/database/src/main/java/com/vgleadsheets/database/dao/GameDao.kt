package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.Flow

interface GameDao : OneToManyDao<Game> {
    fun searchByName(name: String): Flow<List<Game>>
}
