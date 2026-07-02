package com.vgleadsheets.conversion

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <ListType, ReturnType> Flow<List<ListType>>.mapListTo(
    mapper: (ListType) -> ReturnType
): Flow<List<ReturnType>> = map { list ->
        list.map(mapper)
    }
