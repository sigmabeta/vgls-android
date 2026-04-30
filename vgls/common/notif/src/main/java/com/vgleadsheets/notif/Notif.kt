package com.vgleadsheets.notif

import com.squareup.moshi.JsonClass
import net.sigmabeta.sage.appcomm.GenericAction
import net.sigmabeta.sage.ui.StringId

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
