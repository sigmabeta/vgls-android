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
import com.vgleadsheets.tracking.TrackingScreen
import kotlinx.android.synthetic.main.fragment_about.list_content

@SuppressWarnings("TooManyFunctions")
class AboutFragment :
    VglsFragment(),
    SingleTextListModel.EventHandler,
    NameCaptionListModel.EventHandler {
    private val adapter = ComponentAdapter()

    private val hudViewModel: HudViewModel by existingViewModel()

    override fun disablePerfTracking() = true

    override fun getFullLoadTargetTime() = -1L

    override fun onClicked(clicked: NameCaptionListModel) {
        when (clicked.dataId) {
            R.string.label_link_vgls.toLong() -> getFragmentRouter()
                .goToWebUrl(getString(R.string.url_vgls))
            R.string.label_link_giantbomb.toLong() -> getFragmentRouter()
                .goToWebUrl(getString(R.string.url_giantbomb))
            else -> showError("Unimplemented.")
        }
    }

    override fun clearClicked() = Unit

    override fun onClicked(clicked: SingleTextListModel) {
        when (clicked.dataId) {
            R.string.label_link_licenses.toLong() -> getFragmentRouter().showLicenseScreen()
            else -> showError("Unimplemented.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
            resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
            resources.getDimension(R.dimen.margin_medium).toInt()

        list_content.adapter = adapter
        list_content.layoutManager = LinearLayoutManager(context)
        list_content.setInsetListenerForPadding(
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

    override fun getTrackingScreen() = TrackingScreen.ABOUT

    private fun constructList() = listOf(
        SectionHeaderListModel(
            getString(R.string.label_section_about_app)
        ),
        NameCaptionListModel(
            R.string.label_link_vgls.toLong(),
            getString(R.string.label_link_vgls),
            getString(R.string.caption_link_vgls),
            this,
            getPerfScreenName(),
            perfTracker
        ),
        NameCaptionListModel(
            R.string.label_link_giantbomb.toLong(),
            getString(R.string.label_link_giantbomb),
            getString(R.string.caption_link_giantbomb),
            this,
            getPerfScreenName(),
            perfTracker
        ),
        SingleTextListModel(
            R.string.label_link_licenses.toLong(),
            getString(R.string.label_link_licenses),
            this
        )
    )

    companion object {
        fun newInstance() = AboutFragment()
    }
}
