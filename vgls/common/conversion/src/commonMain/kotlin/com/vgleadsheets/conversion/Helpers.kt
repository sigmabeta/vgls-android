package com.vgleadsheets.conversion

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <ListType, ReturnType> Flow<List<ListType>>.mapListTo(
    mapper: suspend (ListType) -> ReturnType
): Flow<List<ReturnType>> = map { list ->
        // A plain Iterable.map can't call a suspend mapper (its lambda isn't suspend), but the
        // Flow.map block is suspend — so map each element in a suspend-capable loop. The mapper is
        // suspend because callers now reach suspend DAO reads (e.g. getOneByIdSync) per item.
        list.map { mapper(it) }
    }
