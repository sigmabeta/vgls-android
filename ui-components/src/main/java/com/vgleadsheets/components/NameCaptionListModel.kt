package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    override val layoutId: Int,
    val name: String,
    val caption: String
): ListModel
