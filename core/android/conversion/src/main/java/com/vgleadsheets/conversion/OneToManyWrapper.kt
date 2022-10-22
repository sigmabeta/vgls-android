package com.vgleadsheets.conversion

import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.dao.OneToManyDao
import com.vgleadsheets.database.mapList
import kotlinx.coroutines.flow.map

abstract class OneToManyWrapper<
    RoomDaoType : RoomDao<EntityType>,
    ModelType,
    EntityType,
    ManyModelType,
    ManyEntityType,
    ManyRoomDaoType,
    ConverterType : OneToManyConverter<ModelType, EntityType, ManyModelType, ManyEntityType, ManyRoomDaoType>,
    ManyConverterType : Converter<ManyModelType, ManyEntityType>
    >(
    private val convert: ConverterType,
    private val manyConverter: ManyConverterType,
    private val roomImpl: RoomDaoType,
    private val manyRoomImpl: ManyRoomDaoType
) : OneToManyDao<ModelType> {

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModelWithRelatedMany(it, manyRoomImpl, manyConverter) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModelWithRelatedMany(it, manyRoomImpl, manyConverter) }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            if (withRelated) {
                convert.entityToModelWithRelatedMany(it, manyRoomImpl, manyConverter)
            } else {
                convert.entityToModel(it)
            }
        }

    override suspend fun insert(models: List<ModelType>) = roomImpl
        .insert(
            models.map {
                convert.modelToEntity(it)
            }
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
