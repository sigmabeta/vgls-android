package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.BuildConfig
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.nav.Modal
import com.vgleadsheets.nav.TopLevel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object MenuOptions {
    fun getListModels(
        shouldShow: Boolean,
        refreshing: Boolean,
        updateTime: Async<Long>,
        onTopLevelScreenLinkClick: (TopLevel) -> Unit,
        onModalScreenLinkClick: (Modal) -> Unit,
        onRandomClick: () -> Unit,
        onRefreshClick: () -> Unit,
        onDebugClick: () -> Unit,
        onPerfClick: () -> Unit,
        resources: Resources,
    ): List<ListModel> = if (shouldShow) {
        getFullOptionsList(
            refreshing,
            updateTime,
            onTopLevelScreenLinkClick,
            onModalScreenLinkClick,
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
        refreshing: Boolean,
        updateTime: Async<Long>,
        onTopLevelScreenLinkClick: (TopLevel) -> Unit,
        onModalScreenLinkClick: (Modal) -> Unit,
        onRandomClick: () -> Unit,
        onRefreshClick: () -> Unit,
        onDebugClick: () -> Unit,
        onPerfClick: () -> Unit,
        resources: Resources,
    ) = listOf(
        MenuItemListModel(
            resources.getString(R.string.label_favorites),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_jam_filled,
            { onTopLevelScreenLinkClick(TopLevel.FAVORITE) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_game),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
            { onTopLevelScreenLinkClick(TopLevel.GAME) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_composer),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
            { onTopLevelScreenLinkClick(TopLevel.COMPOSER) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_by_tag),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_tag_black_24dp,
            { onTopLevelScreenLinkClick(TopLevel.TAG) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_all_songs),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_description_24dp,
            { onTopLevelScreenLinkClick(TopLevel.SONG) }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_random),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_shuffle_24dp,
            { onRandomClick() }
        ),
        MenuItemListModel(
            resources.getString(R.string.label_settings),
            null,
            com.vgleadsheets.ui.icons.R.drawable.ic_settings_black_24dp,
            { onModalScreenLinkClick(Modal.SETTINGS) }
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
                com.vgleadsheets.ui.icons.R.drawable.ic_baseline_speed_24,
                onPerfClick,
            ),
            MenuItemListModel(
                resources.getString(R.string.label_debug),
                null,
                com.vgleadsheets.ui.icons.R.drawable.ic_baseline_warning_24,
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
                com.vgleadsheets.ui.icons.R.drawable.ic_refresh_24dp,
                { onRefreshClick() }
            )
        )
    } else {
        RefreshIndicator.getListModels(
            refreshing,
            resources,
        )
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
