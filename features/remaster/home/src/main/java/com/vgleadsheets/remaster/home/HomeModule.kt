package com.vgleadsheets.remaster.home

import com.vgleadsheets.coroutines.VglsDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

abstract class HomeModule(
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
) {
    protected open fun initialState() = HomeModuleState()

    protected val internalModuleState = MutableStateFlow(initialState())
    val moduleState = internalModuleState.asStateFlow()

    protected abstract fun state(): Flow<HomeModuleState>

    fun setup() {
        state()
            .onEach { newState -> internalModuleState.update { newState } }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }
}
