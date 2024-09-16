package com.vgleadsheets.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.NoopListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

abstract class ListState : VglsState {
    abstract fun title(stringProvider: StringProvider): TitleBarModel
    abstract fun toListItems(stringProvider: StringProvider): List<ListModel>

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun toActual(stringProvider: StringProvider): ListStateActual {
        val title = try {
            title(stringProvider)
        } catch (ex: Exception) {
            TitleBarModel(stringProvider.getString(StringId.ERROR_BROKEN_SCREEN_TITLE))
        }

        val listItems = try {
            toListItems(stringProvider)
                .filter { it !is NoopListModel }
                .toImmutableList()
                
        } catch (ex: Exception) {
            persistentListOf(
                ErrorStateListModel(
                    failedOperationName = "toActual",
                    errorString = stringProvider.getString(StringId.ERROR_BROKEN_SCREEN_TITLE),
                    error = ex
                )
            )
        }

        try {
            checkForDupes(listItems)
        } catch (ex: Exception) {
            val errorMessage = stringProvider.getString(StringId.ERROR_BROKEN_SCREEN_DESC)

            val items = persistentListOf(
                ErrorStateListModel(
                    failedOperationName = "renderScreen",
                    errorString = errorMessage,
                    error = ex,
                )
            )
            return ListStateActual(
                title = title,
                listItems = items,
            )
        }

        return ListStateActual(
            title = title,
            listItems = listItems,
        )
    }

    protected fun <ModelType> LCE<ModelType>.withStandardErrorAndLoading(
        loadingType: LoadingType = LoadingType.SQUARE,
        loadingItemCount: Int = 10,
        loadingWithHeader: Boolean = true,
        loadingHorizScrollable: Boolean = false,
        loadingOperationNameOverride: String? = null,
        content: LCE.Content<ModelType>.() -> List<ListModel>
    ): List<ListModel> {
        return when (this) {
            is LCE.Content -> content()
            is LCE.Error -> error(error)
            is LCE.Loading -> loading(
                operationName = loadingOperationNameOverride ?: this.operationName,
                loadingType = loadingType,
                loadingItemCount = loadingItemCount,
                loadingWithHeader = loadingWithHeader,
                loadingHorizScrollable = loadingHorizScrollable
            )

            LCE.Uninitialized -> emptyList()
        }
    }

    protected fun loading(
        operationName: String,
        loadingType: LoadingType = LoadingType.SQUARE,
        loadingItemCount: Int = 5,
        loadingWithHeader: Boolean = false,
        loadingHorizScrollable: Boolean = false,
    ) = if (loadingWithHeader) {
        listOf(
            LoadingItemListModel(
                loadingType = LoadingType.SECTION_HEADER,
                loadOperationName = "$operationName.section.header",
                loadPositionOffset = 0
            )
        )
    } else {
        emptyList()
    } + loadingItems(loadingHorizScrollable, loadingItemCount, loadingType, operationName)

    private fun loadingItems(
        horizScrollable: Boolean,
        itemCount: Int,
        loadingType: LoadingType,
        operationName: String
    ) = if (horizScrollable) {
        listOf(
            HorizontalScrollerListModel(
                dataId = "$operationName.scroller".hashCode().toLong(),
                scrollingItems = subItems(itemCount, loadingType, operationName).toImmutableList()
            )
        )
    } else {
        subItems(itemCount, loadingType, operationName)
    }

    private fun subItems(
        itemCount: Int,
        loadingType: LoadingType,
        operationName: String
    ) = List(itemCount) { index ->
        LoadingItemListModel(
            loadingType = loadingType,
            loadOperationName = operationName,
            loadPositionOffset = index
        )
    }

    protected fun error(operationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName = operationName,
            errorString = "Failed to load data for $operationName.",
            error = error
        )
    )
}
