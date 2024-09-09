package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.LoadingType
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

    protected abstract fun title(): String?

    protected abstract fun loadingType(): LoadingType

    private var hasEmittedLoader = false

    @Suppress("MagicNumber")
    fun setup() = state()
        .onEach { newState ->
            if (delayManager.shouldDelay() && hasEmittedLoader) {
                delay(Random.nextLong(4000) + 1000)
            }

            if (newState is LCE.Content) {
                hasEmittedLoader = true
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

    protected fun Flow<LCE<HomeModuleState>>.withLoadingState() = onStart {
        emit(
            LCE.Content(
                loadingState()
            )
        )
    }

    protected fun Flow<LCE<HomeModuleState>>.withErrorState() = catch { error ->
        emit(
            LCE.Content(
                errorState(error)
            )
        )
    }

    private fun loadOperationName() = "${this.javaClass.name}.loading"

    private fun loadingState() = loadingStateFromName(this.javaClass.simpleName, title(), loadingType())

    private fun errorState(error: Throwable) = HomeModuleState(
        moduleName = this.javaClass.simpleName,
        shouldShow = true, // TODO Only if debug
        title = null,
        items = listOf(
            ErrorStateListModel(
                failedOperationName = loadOperationName(),
                errorString = "Failed to load data for ${this.javaClass.simpleName}.",
                debugText = error.message ?: "Unknown error."
            )
        ),
    )

    companion object {
        fun loadingStateFromName(name: String, title: String?, loadingType: LoadingType) = HomeModuleState(
            moduleName = name,
            shouldShow = true,
            isLoading = true,
            title = title,
            loadingType = loadingType
        )
    }
}
