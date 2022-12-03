package com.vgleadsheets.database.dao

import com.vgleadsheets.model.alias.ComposerAlias
import kotlinx.coroutines.flow.Flow

interface ComposerAliasDataSource : RegularDataSource<ComposerAlias> {
    fun searchByName(name: String): Flow<List<ComposerAlias>>
}
