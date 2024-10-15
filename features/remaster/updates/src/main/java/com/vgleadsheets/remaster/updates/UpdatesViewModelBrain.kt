package com.vgleadsheets.remaster.updates

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.time.ThreeTenTime
import com.vgleadsheets.ui.StringProvider

class UpdatesViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val scheduler: VglsScheduler,
    private val threeTenTime: ThreeTenTime,
    private val appInfo: AppInfo,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchUpdates()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
        }
    }

    private fun fetchUpdates() {
        updateState {
            (it as State).copy(
                updates = generateUpdates()
            )
        }
    }

    @Suppress("MaxLineLength")
    private fun generateUpdates(): LCE.Content<List<AppUpdate>> {
        val releaseDateMs = appInfo.buildTimeMs ?: 0L
        val releaseDate = threeTenTime.longDateTextFromMillis(releaseDateMs) ?: "Unknown"

        return LCE.Content(
            data = listOf(
                AppUpdate(
                    versionCode = 20000,
                    versionName = "2.0.0",
                    releaseDate = releaseDate,
                    changes = listOf(
                        "Full redesign of the entire app UI.",
                        "App now uses Jetpack Compose toolkit (don't worry if you don't know what this means.)",
                        "Sheets are now downloaded as PDFs, which is faster and takes less space on device storage. This should also result in sharper sheets at different sizes.",
                        "UI has been optimized for large screens: tablets, foldables, etc.",
                        "Numerous performance optimizations to make screens load faster.",
                        "Home screen added, which uses your sheet-reading history (saved on device only) to make some basic recommendations.",
                        "Major improvements to the search feature, which was basically broken before.",
                        "Basically a full rewrite of all functionality. In many cases, this directly makes things better in ways you don't care to read here, but more importantly, new features should be easier to add.",
                    )
                )
            )
        )
    }
}
