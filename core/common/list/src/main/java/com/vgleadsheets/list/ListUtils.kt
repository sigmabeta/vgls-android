package com.vgleadsheets.list

import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

fun checkForDupes(items: ImmutableList<ListModel>) {
    val duplicateIds = items
        .groupingBy { it.dataId }
        .eachCount()
        .filter { it.value > 1 }

    if (duplicateIds.isNotEmpty()) {
        val duplicates = mutableListOf<ListModel>()

        duplicateIds.forEach { duplicateId ->
            duplicates.addAll(
                items.filter { it.dataId == duplicateId.key }
            )
        }

        val duplicatesAsString = duplicates
            .joinToString("\n") {
                it.dataId.toString() + " - " + it.toString()
            }

        val message = "Duplicate ids found: \n$duplicatesAsString"
        throw IllegalArgumentException(message)
    }

    val horizScrollers = items.filterIsInstance<HorizontalScrollerListModel>()
    horizScrollers.forEach {
        checkForDupes(it.scrollingItems)
    }
}
