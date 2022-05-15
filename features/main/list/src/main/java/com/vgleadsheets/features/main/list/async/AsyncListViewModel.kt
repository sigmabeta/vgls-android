package com.vgleadsheets.features.main.list.async

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.mvrx.MvRxViewModel

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class AsyncListViewModel<DataType : ListData, StateType : AsyncListState<DataType>> constructor(
    initialState: StateType
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

    fun onSelectedPartUpdate(newPart: Part) {
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
        selectedPart: Part?
    ): List<ListModel> {
        return createDataListModels(
            data,
            digest,
            updateTime,
            selectedPart
        )
    }

    open fun createFullEmptyStateListModel(): ListModel? = null

    open fun defaultLoadingListModel(index: Int): ListModel =
        LoadingImageNameCaptionListModel("allData", index)

    protected open val showDefaultEmptyState = true

    abstract fun createSuccessListModels(
        data: DataType,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part
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
            )
        )

    @SuppressWarnings("ComplexMethod")
    private fun createDataListModels(
        data: DataType,
        digest: Async<*>,
        updateTime: Async<*>,
        selectedPart: Part?
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
