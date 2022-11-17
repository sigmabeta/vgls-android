package com.vgleadsheets.conversion

interface OneToOneConverter<ModelType, EntityType, ForeignModelType, ForeignEntityType, ForeignDaoType> :
    Converter<ModelType, EntityType> {
    fun EntityType.toModelWithRelatedOne(
        foreignDao: ForeignDaoType,
        converter: Converter<ForeignModelType, ForeignEntityType>
    ): ModelType

    fun ForeignDaoType.getForeignModel(
        foreignId: Long,
        converter: Converter<ForeignModelType, ForeignEntityType>
    ): ForeignModelType

    fun entityToModelWithForeignOne(
        entity: EntityType,
        foreignDao: ForeignDaoType,
        converter: Converter<ForeignModelType, ForeignEntityType>
    ) = entity.toModelWithRelatedOne(foreignDao, converter)
}
