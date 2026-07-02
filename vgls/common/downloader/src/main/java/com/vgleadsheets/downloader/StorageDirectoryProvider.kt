package com.vgleadsheets.downloader

import okio.Path

interface StorageDirectoryProvider {
    fun getStorageDirectory(): Path
}
