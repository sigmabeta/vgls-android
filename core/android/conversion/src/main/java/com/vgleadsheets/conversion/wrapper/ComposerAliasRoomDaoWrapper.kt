package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.converter.ComposerAliasConverter
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.dao.ComposerAliasDao
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.alias.ComposerAlias
import kotlinx.coroutines.flow.map

class ComposerAliasRoomDaoWrapper(
    private val convert: ComposerAliasConverter,
    private val roomImpl: ComposerAliasRoomDao
) : ComposerAliasDao {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModel(it) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModel(it) }

    override fun getAll() = roomImpl
        .getAll()
        .mapList { convert.entityToModel(it) }

    override suspend fun insert(models: List<ComposerAlias>) = roomImpl
        .insert(
            models.map {
                convert.modelToEntity(it)
            }
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
