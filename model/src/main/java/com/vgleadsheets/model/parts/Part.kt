package com.vgleadsheets.model.parts

import com.vgleadsheets.model.pages.Page

data class Part(
    val id: Long,
    val name: String,
    val pages: List<Page>?
)
