package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.resources.ResourceProvider

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class ListViewModel<DataType, StateType : ListState<DataType>> constructor(
    initialState: StateType,
    private val resourceProvider: ResourceProvider
) : MvRxViewModel<StateType>(initialState) {
    fun onSelectedPartUpdate(newPart: PartSelectorItem?) {
        setState {
            updateListState(
                selectedPart = newPart,
                listModels = constructList(
                    data,
                    updateTime,
                    digest,
                    newPart
                )
            ) as StateType
        }
    }

    fun onDigestUpdate(newDigest: Async<*>) {
        setState {
            updateListState(
                digest = newDigest,
                listModels = constructList(
                    data,
                    updateTime,
                    newDigest,
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
                    newTime,
                    digest,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun constructList(
        data: Async<List<DataType>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        return listOf(createTitleListModel()) +
                createContentListModels(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
    }

    open fun createFullEmptyStateListModel(): ListModel? = null

    protected open val showDefaultEmptyState = true

    abstract fun createTitleListModel(): TitleListModel

    abstract fun createSuccessListModels(
        data: List<DataType>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel>

    open fun defaultLoadingListModel(index: Int): ListModel =
        LoadingImageNameCaptionListModel("allData", index)

    private fun createContentListModels(
        data: Async<List<DataType>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ) = when (data) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(data.error)
        is Success ->
            successListModelHelper(selectedPart, data, digest, updateTime)
    }

    private fun successListModelHelper(
        selectedPart: PartSelectorItem?,
        data: Success<List<DataType>>,
        digest: Async<*>,
        updateTime: Async<*>
    ): List<ListModel> {
        return if (selectedPart == null) {
            createErrorStateListModel(
                IllegalArgumentException("No part selected.")
            )
        } else if (data().isEmpty() && showDefaultEmptyState) {
            if (updateTime is Success && digest is Loading) {
                createLoadingListModels()
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
                data(),
                updateTime,
                digest,
                selectedPart
            )
        }
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                defaultLoadingListModel(index)
            )
        }

        return listModels
    }

    private fun createErrorStateListModel(error: Throwable) =
        listOf(ErrorStateListModel("allData", error.message ?: "Unknown Error"))

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
