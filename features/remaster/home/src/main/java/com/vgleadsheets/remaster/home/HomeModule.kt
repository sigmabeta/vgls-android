package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.list.DelayManager
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

abstract class HomeModule(
    val priority: Priority,
    private val delayManager: DelayManager,
) {
    protected open fun initialState(): LCE<HomeModuleState> = LCE.Uninitialized

    protected val internalModuleState = MutableStateFlow(initialState())
    val moduleState = internalModuleState.asStateFlow()

    protected abstract fun state(): Flow<LCE<HomeModuleState>>

    @Suppress("MagicNumber")
    fun setup() = state()
        .onEach { newState ->
            if (delayManager.shouldDelay() && newState is LCE.Content) {
                delay(Random.nextLong(4000) + 1000)
            }
            internalModuleState.update { newState }
        }

    fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }

    protected fun Flow<LCE<HomeModuleState>>.withLoadingState() = onStart { emit(LCE.Loading(loadOperationName())) }

    protected fun Flow<LCE<HomeModuleState>>.withErrorState() = catch { error ->
        emit(
            LCE.Content(
                errorState(error)
            )
        )
    }

    private fun loadOperationName() = "${this.javaClass.name}.loading"

    private fun errorState(error: Throwable) = HomeModuleState(
        moduleName = this.javaClass.simpleName,
        shouldShow = false, // TODO Only if debug
        priority = Priority.HIGHEST,
        title = null,
        items = listOf(
            ErrorStateListModel(
                loadOperationName(),
                "Failed to load data for ${this.javaClass.simpleName} because: ${error.message}",
            )
        )
    )
}
