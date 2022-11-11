package com.vgleadsheets.conversion

interface WithManyConverter<
    ModelType,
    EntityType,
    ManyModelType,
    ManyEntityType,
    ManyDaoType> :
    Converter<ModelType, EntityType> {
    fun toModelFromEntity(entity: EntityType) = entity.toModel()

    fun EntityType.toModelWithMany(
        manyDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ): ModelType

    fun ManyDaoType.getManyModels(
        relationId: Long,
        converter: Converter<ManyModelType, ManyEntityType>
    ): List<ManyModelType>

    fun entityToModelWithMany(
        entity: EntityType,
        joinDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ) = entity.toModelWithMany(joinDao, converter)
}
