package com.vgleadsheets.analytics

import com.vgleadsheets.appcomm.VglsAction

fun VglsAction.isInitAction() = when (this) {
    is VglsAction.InitNoArgs,
    is VglsAction.InitWithId,
    is VglsAction.InitWithString,
    is VglsAction.InitWithPageNumber -> true
    else -> false
}

fun VglsAction.getDetails(): String? {
    if (!isInitAction()) {
        return null
    }

    return when (this) {
        is VglsAction.InitNoArgs -> null
        is VglsAction.InitWithId -> id.toString()
        is VglsAction.InitWithString -> arg
        is VglsAction.InitWithPageNumber -> "$id:$pageNumber"
        else -> null
    }
}
