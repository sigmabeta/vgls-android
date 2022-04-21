package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.MenuLoadingItemListModel
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.PerfTracker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object MenuOptions {
    fun getListModels(
        expanded: Boolean,
        randoming: Boolean,
        refreshing: Boolean,
        updateTime: Async<Long>,
        resources: Resources,
        onScreenLinkClick: (String) -> Unit,
        onRandomClick: () -> Unit,
        onRefreshClick: () -> Unit,
        perfTracker: PerfTracker,
    ) = when {
        expanded -> {
            getFullOptionsList(
                resources,
                onScreenLinkClick,
                perfTracker,
                randoming,
                onRandomClick,
                refreshing,
                updateTime,
                onRefreshClick
            )
        }
        refreshing -> getRefreshingIndicatorListModels(resources, perfTracker)
        else -> emptyList()
    }

    private fun getFullOptionsList(
        resources: Resources,
        onScreenLinkClick: (String) -> Unit,
        perfTracker: PerfTracker,
        randoming: Boolean,
        onRandomClick: () -> Unit,
        refreshing: Boolean,
        updateTime: Async<Long>,
        onRefreshClick: () -> Unit
    ) = listOf(
        MenuItemListModel(
            resources.getString(R.string.label_by_game),
            null,
            R.drawable.ic_album_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_GAME) },
            "",
            perfTracker
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_composer),
            null,
            R.drawable.ic_person_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER) },
            "",
            perfTracker
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_tag),
            null,
            R.drawable.ic_tag_black_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_TAG) },
            "",
            perfTracker
        ),
        MenuItemListModel(
            resources.getString(R.string.label_all_sheets),
            null,
            R.drawable.ic_description_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_SONG) },
            "",
            perfTracker
        ),
        if (randoming) {
            MenuLoadingItemListModel(
                resources.getString(R.string.label_random_loading),
                R.drawable.ic_shuffle_24dp,
                "",
                perfTracker
            )
        } else {
            MenuItemListModel(
                resources.getString(R.string.label_random),
                null,
                R.drawable.ic_shuffle_24dp,
                { onRandomClick() },
                "",
                perfTracker
            )
        },
        MenuItemListModel(
            resources.getString(R.string.label_jams),
            null,
            R.drawable.ic_queue_music_black_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_JAM) },
            "",
            perfTracker
        ),
        MenuItemListModel(
            resources.getString(R.string.label_settings),
            null,
            R.drawable.ic_settings_black_24dp,
            { onScreenLinkClick(HudFragment.MODAL_SCREEN_ID_SETTINGS) },
            "",
            perfTracker
        ),
        if (refreshing) {
            getRefreshIndicatorListModel(resources, perfTracker)
        } else {
            MenuItemListModel(
                resources.getString(R.string.label_refresh),
                resources.getUpdateTimeString(updateTime),
                R.drawable.ic_refresh_24dp,
                { onRefreshClick() },
                "",
                perfTracker
            )
        }
    )

    private fun getRefreshingIndicatorListModels(
        resources: Resources,
        perfTracker: PerfTracker
    ) = listOf(
        getRefreshIndicatorListModel(resources, perfTracker)
    )

    private fun getRefreshIndicatorListModel(
        resources: Resources,
        perfTracker: PerfTracker
    ) = MenuLoadingItemListModel(
        resources.getString(R.string.label_refresh_loading),
        R.drawable.ic_refresh_24dp,
        "",
        perfTracker
    )

    private fun Resources.getUpdateTimeString(updateTime: Async<Long>): String {
        val calendar = Calendar.getInstance()

        if (updateTime !is Success) {
            return "..."
        }

        val checkedTime = updateTime()

        val date = if (checkedTime > 0L) {
            calendar.timeInMillis = checkedTime
            val time = calendar.time
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            dateFormat.format(time)
        } else {
            getString(R.string.date_never)
        }

        return getString(R.string.label_refresh_date, date)
    }
}