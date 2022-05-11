package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfState

object PerfDisplay {
    fun getListModels(
        expanded: Boolean,
        selectedScreen: PerfSpec,
        perfState: PerfState?,
        onScreenSelected: (PerfSpec) -> Unit,
        resources: Resources
    ) = if (expanded && perfState != null) {
        screenPicker(
            selectedScreen,
            onScreenSelected,
            resources
        ) + perfValuesForScreen(
            selectedScreen,
            perfState
        )
    } else {
        emptyList()
    }

    private fun screenPicker(
        selectedScreen: PerfSpec,
        onScreenSelected: (PerfSpec) -> Unit,
        resources: Resources
    ): List<ListModel> {
        val perfSpecs = PerfSpec.values().toList()
        return listOf(
            DropdownSettingListModel(
                "PerfSpec",
                resources.getString(R.string.label_perf_stats_for),
                perfSpecs.indexOf(selectedScreen),
                perfSpecs.map { it.name },
                dropdownHandler { onScreenSelected(it) }
            )
        )
    }

    private fun perfValuesForScreen(
        selectedScreen: PerfSpec,
        perfState: PerfState
    ): List<ListModel> {
        val screen = perfState.screens[selectedScreen]
        return if (screen != null) {
            val stageDurations = screen.stageDurations

            stageDurations.map {
                LabelValueListModel(
                    it.key.name,
                    it.value.toString(),
                    "",
                    noopClicker()
                )
            }
        } else {
            listOf(
                LabelValueListModel(
                    "No perf data recorded.",
                    "",
                    "",
                    noopClicker()
                )
            )
        }
    }

    private fun dropdownHandler(onOptionSelected: (PerfSpec) -> Unit) =
        object : DropdownSettingListModel.EventHandler {
            override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
                onOptionSelected(PerfSpec.values()[selectedPosition])
            }
        }

    private fun noopClicker() = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) {}

        override fun clearClicked() {}

        override fun onLabelValueLoaded(screenName: String) {}
    }
}
