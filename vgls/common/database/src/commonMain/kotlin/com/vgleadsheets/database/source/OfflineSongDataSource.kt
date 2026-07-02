package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.Offline
import kotlinx.coroutines.flow.Flow

interface OfflineSongDataSource {
    suspend fun addOffline(id: Long)

    suspend fun removeOffline(id: Long)

    fun isOfflineSong(id: Long): Flow<Boolean>

    fun getAll(): Flow<List<Offline>>
}
