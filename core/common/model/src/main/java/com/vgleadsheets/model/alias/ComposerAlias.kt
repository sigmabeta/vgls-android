package com.vgleadsheets.model.alias

data class ComposerAlias(
    val id: Long,
    val composerId: Long,
    val name: String,
    val photoUrl: String?
)
