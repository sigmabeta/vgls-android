package com.vgleadsheets.features.main.about

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.existingViewModel
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_about.list_abouts

class AboutFragment : VglsFragment(),
    SingleTextListModel.Handler,
    NameCaptionListModel.EventHandler {
    private val adapter = ComponentAdapter()

    private val hudViewModel: HudViewModel by existingViewModel()

    override fun onClicked(clicked: NameCaptionListModel) {
        showError("Unimplemented.")
    }

    override fun onClicked(clicked: SingleTextListModel) {
        showError("Unimplemented.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_abouts.adapter = adapter
        list_abouts.layoutManager = LinearLayoutManager(context)
        list_abouts.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() {
        hudViewModel.alwaysShowBack()

        val listModels = constructList()
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_about

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList() = listOf(
        SectionHeaderListModel(
            getString(R.string.label_section_about_app)
        ),
        SingleTextListModel(
            R.string.label_link_licenses.toLong(),
            getString(R.string.label_link_licenses),
            this
        ),
        NameCaptionListModel(
            R.string.label_link_giantbomb.toLong(),
            getString(R.string.label_link_giantbomb),
            getString(R.string.caption_link_giantbomb),
            this
        )
    )

    companion object {
        fun newInstance() = AboutFragment()
    }
}
