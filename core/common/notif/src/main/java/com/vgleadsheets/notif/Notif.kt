package com.vgleadsheets.notif

import com.squareup.moshi.JsonClass
import com.vgleadsheets.appcomm.GenericAction
import com.vgleadsheets.ui.StringId

@JsonClass(generateAdapter = true)
data class Notif(
    val id: Long,
    val title: StringId,
    val description: String,
    val actionLabel: String,
    val category: NotifCategory,
    val isOneTime: Boolean,
    val action: GenericAction? = null,
)
