package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlinx.coroutines.yield

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

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection. However, unlike standard `map`,
 * yields in between each element.
 *
 */
@Suppress("MagicNumber")
suspend inline fun <T, R> Iterable<T>.mapYielding(transform: (T) -> R): List<R> {
    return mapToYielding(ArrayList(collectionSizeOrDefault(10)), transform)
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and appends the results to the given [destination]. However, unlike standard `map`,
 * yields in between each element.
 */
@PublishedApi
internal suspend inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.mapToYielding(
    destination: C,
    transform: (T) -> R
): C {
    for (item in this) {
        yield()
        destination.add(transform(item))
    }
    return destination
}

@PublishedApi
internal fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int =
    if (this is Collection<*>) this.size else default

interface ListContent {
    fun failure(): Throwable?

    fun isLoading(): Boolean

    fun hasFailed(): Boolean

    fun isEmpty(): Boolean

    fun isReady(): Boolean

    fun isFullyLoaded() = isReady()
}
