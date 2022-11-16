package com.vgleadsheets.database.dao

import com.vgleadsheets.model.SetlistEntry
import kotlinx.coroutines.flow.Flow

interface SetlistEntryDataSource : OneToManyDataSource<SetlistEntry> {
    fun getSetlistEntriesForJam(jamId: Long): Flow<List<SetlistEntry>>
}
