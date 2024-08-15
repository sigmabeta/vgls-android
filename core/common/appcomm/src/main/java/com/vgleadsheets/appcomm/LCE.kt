package com.vgleadsheets.appcomm

sealed class LCE<out T> {
    data object Uninitialized : LCE<Nothing>()
    data class Loading(val operationName: String) : LCE<Nothing>()
    data class Content<T>(val data: T) : LCE<T>()
    data class Error<T>(val operationName: String, val error: Throwable) : LCE<T>()
}
