package com.vgleadsheets.repository

sealed class Data<out T> {
    open operator fun invoke(): T? = null
}

data class Error<out T>(val error: Throwable) : Data<T>()

class Empty<out T> : Data<T>()

class Network<out T> : Data<T>()

data class Storage<out T>(val data: T) : Data<T>() {
    override operator fun invoke(): T = data
}
