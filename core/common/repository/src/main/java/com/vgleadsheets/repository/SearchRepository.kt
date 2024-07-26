package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.SearchHistoryDataSource

class SearchRepository(
    searchHistoryDataSource: SearchHistoryDataSource,
    songDataSource: SongDataSource,
    gameDataSource: GameDataSource,
    composerDataSource: ComposerDataSource,
) {
}
