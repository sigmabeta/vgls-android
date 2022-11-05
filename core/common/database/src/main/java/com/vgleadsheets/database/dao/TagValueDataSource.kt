package com.vgleadsheets.database.dao

import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue

interface TagValueDataSource : OneToManyDataSource<TagValue> {
    suspend fun insertRelations(relations: List<SongTagValueRelation>)
}
