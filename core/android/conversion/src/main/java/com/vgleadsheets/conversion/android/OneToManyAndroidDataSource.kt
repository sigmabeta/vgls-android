package com.vgleadsheets.conversion.android

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.RoomDao
import com.vgleadsheets.database.dao.OneToManyDataSource
import kotlinx.coroutines.flow.map

abstract class OneToManyAndroidDataSource<
    RoomDaoType : RoomDao<EntityType>,
    ModelType,
    EntityType,
    ManyModelType,
    ManyEntityType,
    ManyRoomDaoType,
    ConverterType : WithManyConverter<ModelType, EntityType, ManyModelType, ManyEntityType, ManyRoomDaoType>,
    ManyConverterType : Converter<ManyModelType, ManyEntityType>
    >(
    private val convert: ConverterType,
    private val manyConverter: ManyConverterType,
    private val roomImpl: RoomDaoType,
    private val manyRoomImpl: ManyRoomDaoType
) : OneToManyDataSource<ModelType> {

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map { convert.entityToModelWithMany(it, manyRoomImpl, manyConverter) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let { convert.entityToModelWithMany(it, manyRoomImpl, manyConverter) }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            if (withRelated) {
                convert.entityToModelWithMany(it, manyRoomImpl, manyConverter)
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

    override fun nukeTable() = roomImpl.nukeTable()
}
