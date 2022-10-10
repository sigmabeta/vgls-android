package com.vgleadsheets.database.enitity

interface OneToManyEntity<ModelType, EntityType, RelatedModelType, RelationDaoType> {
    fun toModel(withRelatedModels: Boolean, relatedDao: RelationDaoType): ModelType

    fun RelationDaoType.getRelatedModels(relationId: Long): List<RelatedModelType>

    fun converter(): ModelToEntityConverter<EntityType, ModelType>
}
