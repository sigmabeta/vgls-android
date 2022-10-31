package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Jam
import kotlinx.coroutines.flow.Flow

interface JamDataSource : RegularDataSource<Jam> {
    fun searchByName(name: String): Flow<List<Jam>>
}
