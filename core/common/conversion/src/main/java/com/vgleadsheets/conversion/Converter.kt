package com.vgleadsheets.conversion

interface Converter<ModelType, EntityType> {
    fun ModelType.toEntity(): EntityType

    fun EntityType.toModel(): ModelType

    fun modelToEntity(model: ModelType): EntityType = model.toEntity()

    fun entityToModel(entity: EntityType): ModelType =
        entity.toModel()

    companion object {
        val ID_OFFSET_COMPOSER = 1_000_000_000L
        val ID_OFFSET_GAME = 100_000L
    }
}
