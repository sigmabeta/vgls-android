package com.vgleadsheets.storage

import com.uber.simplestore.SimpleStore
import io.reactivex.Single

internal class SimpleStorage(val simpleStore: SimpleStore) : Storage {
    override fun getSavedTopLevelScreen() =
        Single.fromFuture(simpleStore.getString(KEY_SELECTED_TOP_LEVEL))

    override fun getSavedSelectedPart() =
        Single.fromFuture(simpleStore.getString(KEY_SELECTED_PART))

    override fun saveTopLevelScreen(screenId: String) = Single.fromFuture(
        simpleStore.putString(KEY_SELECTED_TOP_LEVEL, screenId)
    )

    override fun saveSelectedPart(partId: String) = Single.fromFuture(
        simpleStore.putString(KEY_SELECTED_PART, partId)
    )

    override fun getSettingSheetScreenOn() =
        Single.fromFuture(simpleStore.getString(KEY_SHEETS_KEEP_SCREEN_ON))
            .map { it.toBoolean() }

    override fun saveSettingSheetScreenOn(setting: Boolean) = Single.fromFuture(
        simpleStore.putString(KEY_SHEETS_KEEP_SCREEN_ON, setting.toString())
    )

    override fun getAllSettings(): Single<List<Boolean>> = Single
        .concat(
            getSettingSheetScreenOn(),
            getSettingSheetScreenOn()
        )
        .toList()

    companion object {
        const val KEY_SELECTED_TOP_LEVEL = "KEY_SELECTED_TOP_LEVEL"
        const val KEY_SELECTED_PART = "KEY_SELECTED_PART"

        const val KEY_SHEETS_KEEP_SCREEN_ON = "SETTING_SHEET_KEEP_SCREEN_ON"
    }
}
