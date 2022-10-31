package com.vgleadsheets.model.alias

import com.vgleadsheets.model.Composer

data class ComposerAlias(
    val id: Long,
    val composerId: Long,
    val name: String,
    val photoUrl: String?,
    val composer: Composer?
)
