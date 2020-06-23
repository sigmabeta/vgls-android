package com.vgleadsheets.features.main.list.async


import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.resources.ResourceProvider

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class AsyncListViewModel<DataType : ListData, StateType : AsyncListState<DataType>> constructor(
    initialState: StateType,
    private val resourceProvider: ResourceProvider
) : MvRxViewModel<StateType>(initialState) {
    fun onSelectedPartUpdate(newPart: PartSelectorItem?) {
        setState {
            updateListState(
                selectedPart = newPart,
                listModels = constructList(
                    data,
                    this
                )
            ) as StateType
        }
    }

    fun onDigestUpdate(newDigest: Async<List<*>>) {
        setState {
            updateListState(
                digest = newDigest,
                listModels = constructList(
                    data,
                    this
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
                    this
                )
            ) as StateType
        }
    }

    fun constructList(
        data: DataType,
        state: StateType
    ): List<ListModel> {
        val titleModel = createTitleListModel()
        val titleModelAsList =
            if (titleModel != null) listOf(titleModel) else emptyList()

        val contentListModels = createDataListModels(
            data,
            state.updateTime,
            state.digest,
            state.selectedPart
        )

        return titleModelAsList + contentListModels
    }

    abstract fun createTitleListModel(): TitleListModel?

    abstract fun createFullEmptyStateListModel(): ListModel

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
                LoadingNameCaptionListModel("allData", index)
            )
        }

        return listModels
    }

    protected fun createErrorStateListModel(error: Throwable) =
        listOf(ErrorStateListModel("allData", error.message ?: "Unknown Error"))

    private fun createDataListModels(
        data: DataType,
        digest: Async<*>,
        updateTime: Async<*>,
        selectedPart: PartSelectorItem?
    ) = if (selectedPart == null) {
        createErrorStateListModel(
            IllegalArgumentException("No part selected.")
        )
    } else if (data.isEmpty()) {
        if (digest is Loading || updateTime is Loading) {
            createLoadingListModels()
        } else {
            listOf(
                createFullEmptyStateListModel()
            )
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
