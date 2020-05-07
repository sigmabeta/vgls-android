package com.vgleadsheets.features.main.list.async


import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.resources.ResourceProvider
import timber.log.Timber

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
                    updateTime,
                    digest,
                    newPart
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
        data: DataType,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        Timber.v("Constructing list...")

        val titleModel = createTitleListModel()
        val titleModelAsList =
            if (titleModel != null) listOf(titleModel) else emptyList()

        val contentListModels = createDataListModels(
            data,
            updateTime,
            digest,
            selectedPart
        )

        return titleModelAsList + contentListModels
    }

    abstract fun createTitleListModel(): TitleListModel?

    abstract fun createFullEmptyStateListModel(): EmptyStateListModel

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
                LoadingNameCaptionListModel(index)
            )
        }

        return listModels
    }

    protected fun createErrorStateListModel(error: Throwable) =
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createDataListModels(
        data: DataType,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        val failReason = data.getFailReason()

        return when {
            failReason != null -> createErrorStateListModel(failReason)
            data.isUninitialized() -> createLoadingListModels()
            data.isLoading() -> createLoadingListModels()
            data.isSuccess() -> successListModelHelper(selectedPart, data, digest, updateTime)
            else -> createErrorStateListModel(IllegalStateException("Unhandled ListData state."))
        }
    }

    private fun successListModelHelper(
        selectedPart: PartSelectorItem?,
        data: DataType,
        digest: Async<*>,
        updateTime: Async<*>
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

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
