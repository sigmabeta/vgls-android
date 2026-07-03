package com.vgleadsheets.database.dao

import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagValueDataSource : DataSource<TagValue> {
    suspend fun insertRelations(relations: List<SongTagValueRelation>)

    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>

    suspend fun getTagValuesForTagKeySync(tagKeyId: Long): List<TagValue>

    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>

    suspend fun getTagValuesForSongSync(songId: Long): List<TagValue>
}
