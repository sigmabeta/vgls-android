package com.vgleadsheets.storage

import com.uber.simplestore.SimpleStore
import com.vgleadsheets.common.debug.NetworkEndpoint
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_MISC_PERF_VIEW
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import com.vgleadsheets.storage.Storage.Companion.KEY_SELECTED_PART
import com.vgleadsheets.storage.Storage.Companion.KEY_SELECTED_TOP_LEVEL
import com.vgleadsheets.storage.Storage.Companion.KEY_SHEETS_KEEP_SCREEN_ON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class SimpleStorage(
    private val simpleStore: SimpleStore,
    private val dispatchers: VglsDispatchers
) : Storage {
    private val storageCoroutineScope = CoroutineScope(dispatchers.disk)

    override suspend fun getSavedTopLevelScreen() =
        simpleStore.getString(KEY_SELECTED_TOP_LEVEL).await()

    override suspend fun getSavedSelectedPart() =
        simpleStore.getString(KEY_SELECTED_PART).await()

    override fun saveTopLevelScreen(screenId: String) {
        storageCoroutineScope.launch {
            simpleStore.putString(KEY_SELECTED_TOP_LEVEL, screenId)
        }
    }

    override fun saveSelectedPart(partId: String) {
        storageCoroutineScope.launch {
            simpleStore.putString(KEY_SELECTED_PART, partId)
        }
    }

    override suspend fun getSettingSheetScreenOn() = BooleanSetting(
        KEY_SHEETS_KEEP_SCREEN_ON,
        R.string.label_setting_screen_on,
        simpleStore.getString(KEY_SHEETS_KEEP_SCREEN_ON).await().toBoolean()
    )

    override suspend fun getDebugSettingNetworkEndpoint(): DropdownSetting {
        val fromStorage = simpleStore.getString(KEY_DEBUG_NETWORK_ENDPOINT).await()

        val savedValue = if (fromStorage.isBlank()) {
            NetworkEndpoint.PROD.ordinal
        } else {
            fromStorage.toInt()
        }

        return DropdownSetting(
            KEY_DEBUG_NETWORK_ENDPOINT,
            R.string.label_debug_network_endpoint,
            savedValue,
            NetworkEndpoint.values().map { it.displayStringId }
        )
    }

    override suspend fun getDebugSettingShowPerfView(): BooleanSetting {
        val fromStorage = simpleStore.getString(KEY_DEBUG_MISC_PERF_VIEW).await()

        val savedValue = if (fromStorage.isBlank()) {
            BuildConfig.DEBUG
        } else {
            fromStorage.toBoolean()
        }

        return BooleanSetting(
            KEY_DEBUG_MISC_PERF_VIEW,
            R.string.label_debug_misc_perf_view,
            savedValue
        )
    }

    override fun saveSettingSheetScreenOn(setting: Boolean) {
        storageCoroutineScope.launch {
            simpleStore.putString(KEY_SHEETS_KEEP_SCREEN_ON, setting.toString())
        }
    }

    override suspend fun getAllSettings() = listOf(
        getSettingSheetScreenOn()
    )

    override suspend fun getAllDebugSettings() = listOf(
        getDebugSettingNetworkEndpoint(),
        getDebugSettingShowPerfView()
    )

    override fun saveDebugSelectedNetworkEndpoint(newValue: Int) {
        storageCoroutineScope.launch {
            simpleStore.putString(KEY_DEBUG_NETWORK_ENDPOINT, newValue.toString())
        }
    }

    override fun saveDebugSettingPerfView(newValue: Boolean) {
        storageCoroutineScope.launch {
            simpleStore.putString(KEY_DEBUG_MISC_PERF_VIEW, newValue.toString())
        }
    }
}
