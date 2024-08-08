package com.vgleadsheets.ui.licenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.licenses.R

@Composable
fun LicenseScreen(
    state: State,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
    ) {
        val url = state.licensePageUrl ?: return
        val webState = rememberWebViewState(url)

        val resources = LocalContext.current.resources

        if (webState.errorsForCurrentRequest.isEmpty()) {
            WebView(
                state = webState,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            EmptyListIndicator(
                model = ErrorStateListModel(
                    failedOperationName = "webpageLoad",
                    errorString = resources.getString(R.string.error_webview_failed)
                ),
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
    }
}
