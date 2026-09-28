package com.vgleadsheets.notif

import kotlinx.serialization.Serializable
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.GenericAction

@Serializable
data class Notif(
    val id: Long,
    val title: VglsStringId,
    val description: String,
    val actionLabel: String,
    val category: NotifCategory,
    val isOneTime: Boolean,
    val action: GenericAction? = null,
)
