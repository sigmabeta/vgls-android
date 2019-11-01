package com.vgleadsheets.features.main.settings

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
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import com.vgleadsheets.storage.Setting
import kotlinx.android.synthetic.main.fragment_settings.list_settings
import javax.inject.Inject

class SettingsFragment : VglsFragment(), CheckableListModel.EventHandler,
    SingleTextListModel.Handler {
    override fun onClicked(clicked: SingleTextListModel) {
        showError("Unimplemented.")
    }

    override fun onClicked(clicked: CheckableListModel) {
        viewModel.setSetting(clicked.settingId, !clicked.checked)
    }

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: SettingsViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_settings.adapter = adapter
        list_settings.layoutManager = LinearLayoutManager(context)
        list_settings.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(viewModel) { state ->
        hudViewModel.alwaysShowBack()

        // To prevent flashing
        if (state.settings is Loading) return@withState

        val listModels = constructList(state.settings)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_settings

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
        val sheetsSection = createSection(settings, HEADER_ID_SHEET)
        val miscSection = createMiscSection(settings)

        return sheetsSection + miscSection
    }

    private fun createMiscSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, HEADER_ID_MISC)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_link_about.toLong(),
                getString(R.string.label_link_about),
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
            .map {
                CheckableListModel(
                    it.settingId,
                    getString(it.displayStringId),
                    it.value,
                    this
                )
            }

        return headerModels + settingsModels
    }

    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        HEADER_ID_SHEET -> getString(R.string.section_sheets)
        HEADER_ID_MISC -> getString(R.string.section_misc)
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

        const val HEADER_ID_SHEET = "SETTING_SHEET"
        const val HEADER_ID_MISC = "SETTING_MISC"

        fun newInstance() = SettingsFragment()
    }
}
