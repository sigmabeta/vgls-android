package com.vgleadsheets.downloader

import okio.Path

/**
 * A downloaded (or on-disk) sheet PDF. The location is an [okio.Path] rather than a `java.io.File`
 * so the [SheetDownloader] interface — and this type it returns — stays in commonMain and works on
 * every platform. JVM/Android callers convert to `java.io.File` at the edge (`File(path.toString())`)
 * where the platform PDF renderer needs one.
 */
data class SheetFileResult(
    val path: Path,
    val sourceType: SheetSourceType,
)
