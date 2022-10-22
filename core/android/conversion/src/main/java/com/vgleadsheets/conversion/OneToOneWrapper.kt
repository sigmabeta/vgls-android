package com.vgleadsheets.conversion

import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.dao.OneToManyDao
import com.vgleadsheets.database.mapList
import kotlinx.coroutines.flow.map

abstract class OneToOneWrapper<
    RoomDaoType : RoomDao<EntityType>,
    ModelType,
    EntityType,
    ForeignModelType,
    ForeignEntityType,
    ForeignRoomDaoType,
    ConverterType : OneToOneConverter<ModelType, EntityType, ForeignModelType, ForeignEntityType, ForeignRoomDaoType>,
    ForeignConverterType : Converter<ForeignModelType, ForeignEntityType>
    >(
    private val convert: ConverterType,
    private val foreignConverter: ForeignConverterType,
    private val roomImpl: RoomDaoType,
    private val relatedRoomImpl: ForeignRoomDaoType
) : OneToManyDao<ModelType> {

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter) }

    override fun getAll(withForeign: Boolean) = roomImpl
        .getAll()
        .mapList {
            if (withForeign) {
                convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter)
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
