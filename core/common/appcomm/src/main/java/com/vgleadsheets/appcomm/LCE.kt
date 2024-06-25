package com.vgleadsheets.appcomm

sealed class LCE<T> {
    data object Uninitialized : LCE<Nothing>()
    data object Loading : LCE<Nothing>()
    data class Content<T>(val data : T) : LCE<T>()
    data class Error<T>(val error : Throwable) : LCE<T>()
}
