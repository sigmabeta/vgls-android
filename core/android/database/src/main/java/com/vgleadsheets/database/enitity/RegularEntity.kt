package com.vgleadsheets.database.enitity

interface RegularEntity<ModelType, EntityType> {
    fun toModel(): ModelType

    fun ModelType.toEntity(): EntityType
}
