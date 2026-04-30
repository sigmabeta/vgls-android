package com.vgleadsheets.downloader

import java.io.File

interface StorageDirectoryProvider {
    fun getStorageDirectory(): File
}
