package com.vgleadsheets.database.enitity

interface ModelToEntityConverter<EntityType, ModelType> {
    fun ModelType.toEntity(): EntityType
}
