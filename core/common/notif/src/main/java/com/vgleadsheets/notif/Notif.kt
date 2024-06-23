package com.vgleadsheets.notif

import com.squareup.moshi.JsonClass
import com.vgleadsheets.appcomm.VglsAction

@JsonClass(generateAdapter = true)
data class Notif(
    val id: Long,
    val title: String,
    val description: String,
    val actionLabel: String,
    val category: NotifCategory,
    val isOneTime: Boolean,
    val action: VglsAction?,
)
