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
import kotlinx.android.synthetic.main.fragment_settings.list_settings
import javax.inject.Inject

class SettingsFragment : VglsFragment(), CheckableListModel.EventHandler,
    SingleTextListModel.Handler {
    override fun onClicked(clicked: SingleTextListModel) {
        showError("Unimplemented.")
    }

    override fun onClicked(clicked: CheckableListModel) {
        showError("Unimplemented.")
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

        val listModels = constructList(state.settings)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(settings: Async<List<Boolean>>): List<ListModel> {
        return when (settings) {
            is Loading, Uninitialized -> createLoadingListModels()
            is Fail -> createErrorListModels(settings.error)
            is Success -> createSuccessListModels(settings())
        }
    }

    private fun createSuccessListModels(settings: List<Boolean>): List<ListModel> {
        val sheetsSection = createSection(settings, HEADER_ID_SHEET)
        val miscSection = createMiscSection()
        val sheetsOnString = "Sheets keep screen on"
        val aboutString = "About"
        listOf(
            SectionHeaderListModel("Sheets"),
            CheckableListModel(sheetsOnString.hashCode().toLong(), sheetsOnString, false, this),
            SectionHeaderListModel("Misc"),
            SingleTextListModel(aboutString.hashCode().toLong(), aboutString, this)
        )
    }

    private fun createMiscSection(): List<ListModel> {

    }

    private fun createSection(
        settings: List<Boolean>,
        headerId: String
    ): List<ListModel> {

    }

    private fun createErrorListModels(error: Throwable) =
        listOf<>(ErrorStateListModel(error.message ?: "Unknown Error"))

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

        const val HEADER_ID_SHEET = "SHEET"
        fun newInstance() = SettingsFragment()
    }
}
