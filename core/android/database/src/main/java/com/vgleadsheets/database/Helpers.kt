package com.vgleadsheets.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
    mapper: (ListType) -> ReturnType
): Flow<List<ReturnType>> {
    return map { list ->
        list.map(mapper)
    }
}

const val ROW_PRIMARY_KEY_ID = "id"
