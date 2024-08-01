package com.vgleadsheets.database.dao

import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagValueDataSource : DataSource<TagValue> {
    fun insertRelations(relations: List<SongTagValueRelation>)

    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>

    fun getTagValuesForTagKeySync(tagKeyId: Long): List<TagValue>

    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>
}
