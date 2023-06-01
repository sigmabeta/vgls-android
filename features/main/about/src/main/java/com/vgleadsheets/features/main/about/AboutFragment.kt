package com.vgleadsheets.features.main.about

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.existingViewModel
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import com.vgleadsheets.tracking.TrackingScreen

@SuppressWarnings("TooManyFunctions")
class AboutFragment :
    VglsFragment() {
    private lateinit var adapter: ComponentAdapter

    private val hudViewModel: HudViewModel by existingViewModel()

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.ABOUT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomOffset =
            resources.getDimension(com.vgleadsheets.ui_core.R.dimen.height_bottom_sheet_peek)
                .toInt() +
                resources.getDimension(com.vgleadsheets.ui_core.R.dimen.margin_medium).toInt()

        val content = view.findViewById<RecyclerView>(R.id.list_content)
        adapter = ComponentAdapter(getVglsFragmentTag(), hatchet)

        content.adapter = adapter
        content.layoutManager = LinearLayoutManager(context)
        content.setInsetListenerForPadding(
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

    private fun constructList(): List<ListModel> = listOf(
        SectionHeaderListModel(
            getString(R.string.label_section_about_app)
        ),
        NameCaptionListModel(
            R.string.label_link_vgls.toLong(),
            getString(R.string.label_link_vgls),
            getString(R.string.caption_link_vgls),
        ) {
            getFragmentRouter().goToWebUrl(getString(R.string.url_vgls))
        },
        NameCaptionListModel(
            R.string.label_link_giantbomb.toLong(),
            getString(R.string.label_link_giantbomb),
            getString(R.string.caption_link_giantbomb),
        ) {
            getFragmentRouter().goToWebUrl(getString(R.string.url_giantbomb))
        },
        SingleTextListModel(
            R.string.label_link_licenses.toLong(),
            getString(R.string.label_link_licenses)
        ) {
            getFragmentRouter().showLicenseScreen()
        }
    )

    companion object {
        fun newInstance() = AboutFragment()
    }
}
