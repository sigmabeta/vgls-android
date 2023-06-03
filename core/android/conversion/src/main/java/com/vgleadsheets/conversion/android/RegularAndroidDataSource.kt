package com.vgleadsheets.conversion.android

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.dao.RegularDataSource
import kotlinx.coroutines.flow.map

abstract class RegularAndroidDataSource<
    RoomDaoType : RoomDao<EntityType>,
    ModelType,
    EntityType,
    ConverterType : Converter<ModelType, EntityType>
    >(
    private val convert: ConverterType,
    private val roomImpl: RoomDaoType,
) : RegularDataSource<ModelType> {

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModel(it) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModel(it) }

    override fun getAll() = roomImpl
        .getAll()
        .mapList { convert.entityToModel(it) }

    override fun insert(models: List<ModelType>) = roomImpl
        .insert(
            models.map {
                convert.modelToEntity(it)
            }
        )

    override fun remove(ids: List<Long>) = roomImpl
        .remove(
            ids.map { DeletionId(it) }
        )

    override fun nukeTable() = roomImpl.nukeTable()
}
