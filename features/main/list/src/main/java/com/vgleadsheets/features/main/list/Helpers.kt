package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <ContentType> Async<ContentType>.content() = invoke()

fun <ContentType> Async<ContentType>.failure() = (this as? Fail)?.error

@OptIn(ExperimentalContracts::class)
fun <ContentType> Async<ContentType>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is Loading<*>)
    }

    return this is Loading
}

@OptIn(ExperimentalContracts::class)
fun <ContentType> Async<ContentType>.hasFailed(): Boolean {
    contract {
        returns(true) implies (this@hasFailed is Fail)
    }

    return this is Fail
}

@OptIn(ExperimentalContracts::class)
fun <ContentType> Async<ContentType>.isReady(): Boolean {
    contract {
        returns(false) implies (this@isReady is Success<*>)
    }

    return this is Success<*>
}

@OptIn(ExperimentalContracts::class)
fun ListContent?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }

    return this == null || this.isEmpty()
}

interface ListContent {
    fun failure(): Throwable?

    fun isLoading(): Boolean

    fun hasFailed(): Boolean

    fun isEmpty(): Boolean

    fun isReady(): Boolean

    fun isFullyLoaded() = isReady()
}
