package com.vgleadsheets.conversion

interface ManyToManyConverter<ModelType, EntityType, ManyModelType, ManyEntityType, ManyDaoType> :
    Converter<ModelType, EntityType> {
    fun toModelFromEntity(entity: EntityType) = entity.toModel()

    fun EntityType.toModelWithJoinedMany(
        manyDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ): ModelType

    fun ManyDaoType.getJoinedModels(
        relationId: Long,
        converter: Converter<ManyModelType, ManyEntityType>
    ): List<ManyModelType>

    fun entityToModelWithJoinedMany(
        entity: EntityType,
        joinDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ) = entity.toModelWithJoinedMany(joinDao, converter)
}
