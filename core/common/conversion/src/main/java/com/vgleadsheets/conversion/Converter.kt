package com.vgleadsheets.conversion

interface Converter<ModelType, EntityType> {
    fun ModelType.toEntity(): EntityType

    fun EntityType.toModel(): ModelType

    fun modelToEntity(model: ModelType): EntityType = model.toEntity()

    fun entityToModel(entity: EntityType): ModelType =
        entity.toModel()
}
