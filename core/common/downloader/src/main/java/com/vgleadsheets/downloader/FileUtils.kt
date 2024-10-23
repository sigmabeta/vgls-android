package com.vgleadsheets.downloader

import java.io.File

object FileUtils {
    fun fileReference(
        storageDirectory: File,
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ) = File(
        storageDirectory,
        "pdfs/$fileName/$partApiId${isAlternate.altSuffix()}.pdf"
    )
}

fun Boolean.altSuffix() = if (this) {
    " [ALT]"
} else {
    ""
}

fun File.ensureExists() {
    if (exists()) {
        return
    }

    mkdir()
}
