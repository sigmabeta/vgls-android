package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.MenuLoadingItemListModel
import com.vgleadsheets.features.main.hud.BuildConfig
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("LongParameterList")
object MenuOptions {
    fun getListModels(
        expanded: Boolean,
        randoming: Boolean,
        refreshing: Boolean,
        updateTime: Async<Long>,
        onScreenLinkClick: (String) -> Unit,
        onRandomClick: () -> Unit,
        onRefreshClick: () -> Unit,
        onDebugClick: () -> Unit,
        onPerfClick: () -> Unit,
        resources: Resources,
    ) = if (expanded) {
        getFullOptionsList(
            randoming,
            refreshing,
            updateTime,
            onScreenLinkClick,
            onRandomClick,
            onRefreshClick,
            onDebugClick,
            onPerfClick,
            resources,
        )
    } else {
        emptyList()
    }

    @Suppress("LongMethod")
    private fun getFullOptionsList(
        randoming: Boolean,
        refreshing: Boolean,
        updateTime: Async<Long>,
        onScreenLinkClick: (String) -> Unit,
        onRandomClick: () -> Unit,
        onRefreshClick: () -> Unit,
        onDebugClick: () -> Unit,
        onPerfClick: () -> Unit,
        resources: Resources,
    ) = listOf(
        MenuItemListModel(
            resources.getString(R.string.label_by_game),
            null,
            R.drawable.ic_album_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_GAME) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_composer),
            null,
            R.drawable.ic_person_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_tag),
            null,
            R.drawable.ic_tag_black_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_TAG) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_all_sheets),
            null,
            R.drawable.ic_description_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_SONG) }
        ),
        if (randoming) {
            MenuLoadingItemListModel(
                resources.getString(R.string.label_random_loading),
                R.drawable.ic_shuffle_24dp,
            )
        } else {
            MenuItemListModel(
                resources.getString(R.string.label_random),
                null,
                R.drawable.ic_shuffle_24dp,
                { onRandomClick() })
        },
        MenuItemListModel(
            resources.getString(R.string.label_jams),
            null,
            R.drawable.ic_queue_music_black_24dp,
            { onScreenLinkClick(HudFragment.TOP_LEVEL_SCREEN_ID_JAM) }
        ),

        MenuItemListModel(
            resources.getString(R.string.label_settings),
            null,
            R.drawable.ic_settings_black_24dp,
            { onScreenLinkClick(HudFragment.MODAL_SCREEN_ID_SETTINGS) }
        )
    ) + getRefreshOptionListModels(
        refreshing,
        updateTime,
        onRefreshClick,
        resources,
    ) + getDebugScreenListModels(
        onDebugClick,
        onPerfClick,
        resources,
    )

    private fun getDebugScreenListModels(
        onDebugClick: () -> Unit,
        onPerfClick: () -> Unit,
        resources: Resources,
    ) = if (BuildConfig.DEBUG) {
        listOf(
            MenuItemListModel(
                resources.getString(R.string.label_perf),
                null,
                R.drawable.ic_baseline_speed_24,
                onPerfClick,
            ),
            MenuItemListModel(
                resources.getString(R.string.label_debug),
                null,
                R.drawable.ic_baseline_warning_24,
                onDebugClick,
            )
        )
    } else {
        emptyList()
    }

    private fun getRefreshOptionListModels(
        refreshing: Boolean,
        updateTime: Async<Long>,
        onRefreshClick: () -> Unit,
        resources: Resources,
    ) = if (!refreshing) {
        listOf(
            MenuItemListModel(
                resources.getString(R.string.label_refresh),
                resources.getUpdateTimeString(updateTime),
                R.drawable.ic_refresh_24dp,
                { onRefreshClick() }
            )
        )
    } else {
        emptyList()
    }

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
