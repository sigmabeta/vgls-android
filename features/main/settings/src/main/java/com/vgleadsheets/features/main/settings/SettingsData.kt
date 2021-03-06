package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.async.ListData
import com.vgleadsheets.storage.Setting

data class SettingsData(
    val settings: Async<List<Setting>> = Uninitialized
) : ListData {
    override fun isEmpty() = !(settings is Success && settings()?.isNotEmpty() == true)

    override fun canShowPartialData() = false
}
