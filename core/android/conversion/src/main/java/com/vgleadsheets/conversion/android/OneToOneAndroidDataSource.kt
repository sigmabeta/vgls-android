package com.vgleadsheets.conversion.android

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.dao.OneToManyDataSource
import kotlinx.coroutines.flow.map

abstract class OneToOneAndroidDataSource<
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
) : OneToManyDataSource<ModelType> {

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter) }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            if (withRelated) {
                convert.entityToModelWithForeignOne(it, relatedRoomImpl, foreignConverter)
            } else {
                convert.entityToModel(it)
            }
        }

    override fun insert(models: List<ModelType>) = roomImpl
        .insert(
            models.map {
                convert.modelToEntity(it)
            }
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
