package com.vgleadsheets.list

import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NoopListModel

fun checkForDupes(items: List<ListModel>) {
    val duplicateIds = items
        .groupingBy { it.dataId }
        .eachCount()
        .filter { it.value > 1 }

    if (duplicateIds.isNotEmpty()) {
        val duplicatesAsString = duplicateIds
            .toList()
            .joinToString("\n") { pair ->
                val item = items.firstOrNull { it.dataId == pair.first }
                "ID ${pair.first} - ${pair.second} times\n" +
                    "Details: $item"
            }

        val message = "Duplicate ids found.\n$duplicatesAsString"
        throw IllegalArgumentException(message)
    }

    val horizScrollers = items.filterIsInstance<HorizontalScrollerListModel>()
    horizScrollers.forEach {
        checkForDupes(it.scrollingItems)
    }
}

fun ifTrue(condition: Boolean, content: () -> ListModel): ListModel {
    return if (condition) {
        content()
    } else {
        NoopListModel
    }
}

fun <InputType> ifNotNull(input: InputType?, content: (InputType) -> ListModel): ListModel {
    return if (input != null) {
        content(input)
    } else {
        NoopListModel
    }
}
