package com.vgleadsheets.features.main.debug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting
import kotlinx.android.synthetic.main.fragment_debug.list_debug
import javax.inject.Inject

class DebugFragment : VglsFragment(), CheckableListModel.EventHandler,
    SingleTextListModel.Handler, DropdownSettingListModel.EventHandler {
    @Inject
    lateinit var debugViewModelFactory: DebugViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: DebugViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: SingleTextListModel) {
        when (clicked.dataId.toInt()) {
            R.string.label_database_clear_sheets -> viewModel.clearSheets()
            R.string.label_database_clear_jams -> viewModel.clearJams()
        }
    }

    override fun onClicked(clicked: CheckableListModel) {
        TODO("Implement this!")
    }

    override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
        viewModel.setDropdownSetting(settingId, selectedPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_debug.adapter = adapter
        list_debug.layoutManager = LinearLayoutManager(context)
        list_debug.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )

        viewModel.selectSubscribe(
            DebugState::changed,
            deliveryMode = uniqueOnly("changed")
        ) { changed ->
            if (changed) {
                showSnackbar(getString(R.string.snack_restart_on_exit))
            }
        }

        viewModel.asyncSubscribe(
            DebugState::jamDeletion,
            deliveryMode = uniqueOnly("jamDeletion")
        ) {
            showSnackbar("Jams cleared.")
        }

        viewModel.asyncSubscribe(
            DebugState::sheetDeletion,
            deliveryMode = uniqueOnly("sheetDeletion")
        ) {
            showSnackbar("Sheets cleared.")
        }
    }

    override fun onBackPress() = withState(viewModel) {
        if (it.changed) {
            getFragmentRouter().restartApp()
            return@withState false
        }
        return@withState true
    }

    override fun invalidate() = withState(viewModel) { state ->
        hudViewModel.alwaysShowBack()

        // To prevent flashing
        if (state.settings is Loading) return@withState

        val listModels = constructList(state.settings)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_debug

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(settings: Async<List<Setting>>): List<ListModel> {
        return when (settings) {
            is Uninitialized -> createLoadingListModels()
            is Fail -> createErrorListModels(settings.error)
            is Success -> createSuccessListModels(settings())
            else -> throw IllegalStateException()
        }
    }

    private fun createSuccessListModels(settings: List<Setting>): List<ListModel> {
        val networkSection = createSection(settings, HEADER_ID_NETWORK)
        val databaseSection = createDatabaseSection(settings)
        return networkSection + databaseSection
    }

    private fun createDatabaseSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, HEADER_ID_DATABASE)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_database_clear_sheets.toLong(),
                getString(R.string.label_database_clear_sheets),
                this
            ),
            SingleTextListModel(
                R.string.label_database_clear_jams.toLong(),
                getString(R.string.label_database_clear_jams),
                this
            )
        )

        return normalItems + customItems
    }

    private fun createSection(
        settings: List<Setting>,
        headerId: String
    ): List<ListModel> {
        val headerModels = listOf(
            SectionHeaderListModel(
                getSectionHeaderString(headerId)
            )
        )

        val settingsModels = settings
            .filter { it.settingId.startsWith(headerId) }
            .map { setting ->
                when (setting) {
                    is BooleanSetting -> CheckableListModel(
                        setting.settingId,
                        getString(setting.labelStringId),
                        setting.value,
                        this
                    )
                    is DropdownSetting -> DropdownSettingListModel(
                        setting.settingId,
                        getString(setting.labelStringId),
                        setting.selectedPosition,
                        setting.valueStringIds.map { getString(it) },
                        this
                    )
                }
            }

        return headerModels + settingsModels
    }

    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        HEADER_ID_NETWORK -> getString(R.string.section_network)
        HEADER_ID_DATABASE -> getString(R.string.section_database)
        else -> throw IllegalArgumentException()
    }

    private fun createErrorListModels(error: Throwable) =
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createLoadingListModels(): ArrayList<ListModel> {
        val loadingModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            loadingModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return loadingModels
    }

    companion object {
        const val LOADING_ITEMS = 4

        const val HEADER_ID_NETWORK = "DEBUG_NETWORK"
        const val HEADER_ID_DATABASE = "DEBUG_DATABASE"

        fun newInstance() = DebugFragment()
    }
}
