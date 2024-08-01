package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource

class TagRepository(
    private val tagKeyDataSource: TagKeyDataSource,
    private val tagValueDataSource: TagValueDataSource,
) {
    fun getAllTagKeys() = tagKeyDataSource
        .getAll()
        .mapListTo { tagKey ->
            tagKey.copy(
                values = getTagValuesForTagKeySync(tagKeyId = tagKey.id)
            )
        }

    fun getTagValuesForTagKey(tagKeyId: Long) = tagValueDataSource
        .getTagValuesForTagKey(tagKeyId)

    fun getTagValuesForTagKeySync(tagKeyId: Long) = tagValueDataSource
        .getTagValuesForTagKeySync(tagKeyId)

    fun getTagValuesForSong(songId: Long) = tagValueDataSource
        .getTagValuesForSong(songId)

    fun getTagKey(tagKeyId: Long) = tagKeyDataSource
        .getOneById(tagKeyId)

    fun getTagValue(tagValueId: Long) = tagValueDataSource
        .getOneById(tagValueId)
}
