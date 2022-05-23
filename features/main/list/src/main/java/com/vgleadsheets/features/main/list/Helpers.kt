package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success

fun <DataType> Async<List<DataType>>.data() = (this as? Success)?.invoke()

fun <DataType> Async<List<DataType>>.failure() = (this as? Fail)?.error
