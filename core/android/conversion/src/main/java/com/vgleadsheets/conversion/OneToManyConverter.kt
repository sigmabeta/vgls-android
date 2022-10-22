package com.vgleadsheets.conversion

interface OneToManyConverter<ModelType, EntityType, ManyModelType, ManyEntityType, ManyDaoType> :
    Converter<ModelType, EntityType> {
    fun EntityType.toModelWithRelatedMany(
        manyDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ): ModelType

    fun ManyDaoType.getRelatedModels(
        relationId: Long,
        converter: Converter<ManyModelType, ManyEntityType>
    ): List<ManyModelType>

    fun entityToModelWithRelatedMany(
        entity: EntityType,
        manyDao: ManyDaoType,
        converter: Converter<ManyModelType, ManyEntityType>
    ): ModelType =
        entity.toModelWithRelatedMany(manyDao, converter)
}
