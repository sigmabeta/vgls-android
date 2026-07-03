package com.vgleadsheets.ui.licenses

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import net.sigmabeta.sage.components.ErrorStateListModel

// Desktop has no WebView yet; show a placeholder (a native/JCEF licenses view is a follow-up).
@Composable
actual fun LicenseScreen(
    state: State,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        EmptyListIndicator(
            model = ErrorStateListModel(
                failedOperationName = "webpageLoad",
                errorString = VglsStringId.ERROR_WEBVIEW_FAILED.text(),
                error = RuntimeException("Licenses are not viewable on desktop yet."),
            ),
            showDebug = false,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
