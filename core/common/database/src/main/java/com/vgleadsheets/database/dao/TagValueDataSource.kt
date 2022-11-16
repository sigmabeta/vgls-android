package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Song
import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagValueDataSource : OneToManyDataSource<TagValue> {
    fun insertRelations(relations: List<SongTagValueRelation>)

    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>
}
