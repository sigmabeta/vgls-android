package com.vgleadsheets.conversion

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
    mapper: (ListType) -> ReturnType
): Flow<List<ReturnType>> {
    return map { list ->
        list.map(mapper)
    }
}
