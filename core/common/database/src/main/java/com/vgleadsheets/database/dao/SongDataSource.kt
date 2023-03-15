package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface SongDataSource : OneToManyDataSource<Song> {
    fun searchByName(name: String): Flow<List<Song>>

    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>

    fun incrementPlayCount(songId: Long)

    fun toggleFavorite(songId: Long)

    fun toggleOffline(songId: Long)
}
