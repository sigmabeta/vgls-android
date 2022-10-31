package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource : OneToManyDataSource<Song> {
    fun searchByName(name: String): Flow<List<Song>>
}
