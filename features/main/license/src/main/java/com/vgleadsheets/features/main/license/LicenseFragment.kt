package com.vgleadsheets.features.main.license

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen

@Suppress("Deprecation")
class LicenseFragment : VglsFragment() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
            resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
            resources.getDimension(R.dimen.margin_medium).toInt()

        val webview = view.findViewById<WebView>(R.id.web_license)
        webview.setOnApplyWindowInsetsListener { _, insets ->
            val density = resources.displayMetrics.density
            val topPadding = (insets.systemWindowInsetTop + topOffset) / density
            val bottomPadding = (insets.systemWindowInsetBottom + bottomOffset) / density

            webview.settings.javaScriptEnabled = true
            webview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(web: WebView, url: String) {
                    val javascript =
                        "javascript:(function(){ document.body.style.paddingTop = '${topPadding}px';" +
                            "document.body.style.paddingBottom = '${bottomPadding}px';})();"
                    web.loadUrl(javascript)
                    web.settings.javaScriptEnabled = false
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url?.toString()

                    if (url?.contains("http") == true) {
                        getFragmentRouter().goToWebUrl(url)
                        return true
                    }

                    return false
                }
            }

            webview.loadUrl("file:///android_asset/open_source_licenses.html")

            insets
        }
    }

    override fun invalidate() = Unit

    override fun getLayoutId() = R.layout.fragment_license

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun getTrackingScreen() = TrackingScreen.LICENSE

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.LICENSE

    companion object {
        fun newInstance() = LicenseFragment()
    }
}
