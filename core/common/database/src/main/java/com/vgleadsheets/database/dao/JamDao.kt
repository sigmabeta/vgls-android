package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Jam
import kotlinx.coroutines.flow.Flow

interface JamDao : RegularDao<Jam> {
    fun searchByName(name: String): Flow<List<Jam>>
}
