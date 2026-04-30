package com.vgleadsheets.urlinfo

data class UrlInfo(
    val loaded: Boolean = false,
    val baseBaseUrl: String? = null,
    val apiBaseUrl: String? = null,
    val imageBaseUrl: String? = null,
    val pdfBaseUrl: String? = null,
    val partId: String? = null,
)
