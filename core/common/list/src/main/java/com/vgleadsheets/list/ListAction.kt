package com.vgleadsheets.list

open class ListAction {
    data object InitNoArgs: ListAction()
    data class InitWithId(val id: Long): ListAction()
    data class InitWithString(val arg: String): ListAction()

    data object Resume: ListAction()
}
