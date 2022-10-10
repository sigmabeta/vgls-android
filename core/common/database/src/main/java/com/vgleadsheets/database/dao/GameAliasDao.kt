package com.vgleadsheets.database.dao

import kotlinx.coroutines.flow.Flow

interface GameAliasDao {

    fun getAliasesByName(name: String): Flow<List<String>>

    suspend fun insertAll(aliases: Map<Long, String>)

    suspend fun nukeTable()
}
