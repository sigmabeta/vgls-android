package com.vgleadsheets.features.main.list.async

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.tracking.api.PerfTracker

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class AsyncListViewModel<DataType : ListData, StateType : AsyncListState<DataType>> constructor(
    initialState: StateType,
    private val screenName: String,
    private val perfTracker: PerfTracker
) : MvRxViewModel<StateType>(initialState) {
    fun onDigestUpdate(newDigest: Async<*>) {
        setState {
            updateListState(
                digest = newDigest,
                listModels = constructList(
                    data,
                    newDigest,
                    updateTime,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun onTimeUpdate(newTime: Async<*>) {
        setState {
            updateListState(
                updateTime = newTime,
                listModels = constructList(
                    data,
                    digest,
                    updateTime,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun onSelectedPartUpdate(newPart: PartSelectorItem?) {
        setState {
            updateListState(
                selectedPart = newPart,
                listModels = constructList(
                    data,
                    digest,
                    updateTime,
                    newPart
                )
            ) as StateType
        }
    }

    fun constructList(
        data: DataType,
        digest: Async<*>,
        updateTime: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        val contentListModels = createDataListModels(
            data,
            digest,
            updateTime,
            selectedPart
        )

        return contentListModels
    }

    open fun createFullEmptyStateListModel(): ListModel? = null

    open fun defaultLoadingListModel(index: Int): ListModel =
        LoadingImageNameCaptionListModel("allData", index)

    protected open val showDefaultEmptyState = true

    protected val cancelPerfOnEmptyState = object : EmptyStateListModel.EventHandler {
        override fun onEmptyStateLoadComplete(screenName: String) {
            perfTracker.cancel(screenName)
        }
    }

    protected val cancelPerfOnErrorState = object : ErrorStateListModel.EventHandler {
        override fun onErrorStateLoadComplete(screenName: String) {
            perfTracker.cancel(screenName)
        }
    }

    abstract fun createSuccessListModels(
        data: DataType,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel>

    protected fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                defaultLoadingListModel(index)
            )
        }

        return listModels
    }

    protected fun createErrorStateListModel(error: Throwable) =
        listOf(
            ErrorStateListModel(
                "allData",
                error.message ?: "Unknown Error",
                screenName,
                cancelPerfOnErrorState
            )
        )

    @SuppressWarnings("ComplexMethod")
    private fun createDataListModels(
        data: DataType,
        digest: Async<*>,
        updateTime: Async<*>,
        selectedPart: PartSelectorItem?
    ) = if (selectedPart == null) {
        createErrorStateListModel(
            IllegalArgumentException("No part selected.")
        )
    } else if (data.isEmpty() && showDefaultEmptyState) {
        if (digest !is Success || updateTime !is Fail) {
            if (data.canShowPartialData()) {
                createSuccessListModels(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
            } else {
                createLoadingListModels()
            }
        } else {
            val emptyState = createFullEmptyStateListModel()
            if (emptyState != null) {
                listOf(
                    emptyState
                )
            } else {
                emptyList()
            }
        }
    } else {
        createSuccessListModels(
            data,
            updateTime,
            digest,
            selectedPart
        )
    }

    companion object {
        const val LOADING_ITEMS = 15
    }
}
