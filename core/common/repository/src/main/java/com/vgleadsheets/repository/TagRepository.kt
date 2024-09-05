package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import kotlinx.coroutines.flow.map

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

    fun getDetailTagKeys() = tagKeyDataSource
        .getAll()
        .mapListTo { tagKey ->
            tagKey.copy(
                values = getTagValuesForTagKeySync(tagKeyId = tagKey.id)
            )
        }
        .map { list ->
            list.filter { !(it.isDifficultyTag() ?: false) }
        }

    fun getDifficultyTagKeys() = tagKeyDataSource
        .getAll()
        .mapListTo { tagKey ->
            tagKey.copy(
                values = getTagValuesForTagKeySync(tagKeyId = tagKey.id)
            )
        }
        .map { list ->
            list.filter { it.isDifficultyTag() ?: false }
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

    fun getIdOfPublishDateTagKey() = tagKeyDataSource
        .getAll()
        .map { list ->
            list.firstOrNull {
                it.name == TAG_NAME_DATE_PUBLISHED
            }
        }
        .map { it?.id }

    companion object {
        const val TAG_NAME_DATE_PUBLISHED = "Date Published"
    }
}
