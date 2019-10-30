package com.vgleadsheets.features.main.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
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

    override fun invalidate() = withState(viewModel) { _ ->
        val listModels = constructList()
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(): List<ListModel> {
        val sheetsOnString = "Sheets keep screen on"
        val aboutString = "About"
        return listOf(
            SectionHeaderListModel("Sheets"),
            CheckableListModel(sheetsOnString.hashCode().toLong(), sheetsOnString, false, this),
            SectionHeaderListModel("Misc"),
            SingleTextListModel(aboutString.hashCode().toLong(), aboutString, this)
        )
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
