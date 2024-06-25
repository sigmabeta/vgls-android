package com.vgleadsheets.appcomm

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericAction(
    val type: String,
    val argIdOne: Long? = null,
    val argIdTwo: Long? = null,
    val argString: String? = null
)
