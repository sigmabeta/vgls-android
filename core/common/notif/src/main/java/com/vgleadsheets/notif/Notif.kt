package com.vgleadsheets.notif

import com.vgleadsheets.appcomm.VglsAction

data class Notif(
    val id: Long,
    val title: String,
    val description: String,
    val actionLabel: String,
    val category: NotifCategory,
    val action: VglsAction?
)
