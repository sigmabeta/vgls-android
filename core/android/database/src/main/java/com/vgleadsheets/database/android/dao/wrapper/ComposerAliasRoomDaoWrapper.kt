package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.dao.ComposerAliasDao
import com.vgleadsheets.database.enitity.ComposerAliasEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComposerAliasRoomDaoWrapper(
    private val roomImpl: ComposerAliasRoomDao
) : ComposerAliasDao {
    override fun getAliasesByName(name: String): Flow<List<String>> = roomImpl
        .getAliasesByName(name)
        .map { list ->
            list.map { entity -> entity.name }
        }

    override suspend fun insertAll(aliases: Map<Long, String>) = roomImpl
        .insertAll(
            aliases.map { ComposerAliasEntity(it.key, it.value) }
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
