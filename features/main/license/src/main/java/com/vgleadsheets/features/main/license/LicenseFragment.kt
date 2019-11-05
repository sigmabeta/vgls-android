package com.vgleadsheets.features.main.license

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.vgleadsheets.VglsFragment
import kotlinx.android.synthetic.main.fragment_license.web_license


class LicenseFragment : VglsFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()

        web_license.loadUrl("file:///android_asset/open_source_licenses.html")

        web_license.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(web: WebView, url: String) {
                web.loadUrl("javascript:(function(){ document.body.style.paddingTop = '${topOffset}px'})();")
            }
        })

    }

    override fun invalidate() {

    }

    override fun getLayoutId() = R.layout.fragment_license

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        fun newInstance() = LicenseFragment()
    }
}
