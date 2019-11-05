package com.vgleadsheets.features.main.license

import android.os.Bundle
import android.view.View
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.setInsetListenerForMargin
import kotlinx.android.synthetic.main.fragment_license.web_license


class LicenseFragment : VglsFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt()

        web_license.setInsetListenerForMargin(topOffset = topOffset, bottomOffset = bottomOffset)

        web_license.loadUrl("file:///android_asset/open_source_licenses.html")
    }

    override fun invalidate() {

    }

    override fun getLayoutId() = R.layout.fragment_license

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        fun newInstance() = LicenseFragment()
    }
}
