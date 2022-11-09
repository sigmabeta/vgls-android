package com.vgleadsheets.database.dao

import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue

interface TagValueDataSource : OneToManyDataSource<TagValue> {
    fun insertRelations(relations: List<SongTagValueRelation>)
}
