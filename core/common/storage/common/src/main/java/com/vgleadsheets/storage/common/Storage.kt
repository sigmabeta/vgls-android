package com.vgleadsheets.storage.common

import kotlinx.coroutines.flow.Flow

interface Storage {
    fun saveString(key: String, value: String)

    fun savedStringFlow(key: String): Flow<String?>

    fun saveInt(key: String, value: Int)

    fun savedIntFlow(key: String): Flow<Int?>
}
