package com.vgleadsheets.downloader

import okio.FileSystem
import okio.Path

object FileUtils {
    fun fileReference(
        storageDirectory: Path,
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ): Path = storageDirectory / "pdfs" / fileName / "$partApiId${isAlternate.altSuffix()}.pdf"
}

fun Boolean.altSuffix() = if (this) {
    " [ALT]"
} else {
    ""
}

/** Create this directory (and any missing parents). */
fun Path.ensureDirectoryExists() {
    FileSystem.SYSTEM.createDirectories(this)
}
