@file:OptIn(ExperimentalMaterial3Api::class)

package com.vgleadsheets.topbar

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun RemasterTopBar(
    state: TopBarState,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val resources = LocalContext.current.resources

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        scrollBehavior = scrollBehavior,
        title = {
            Crossfade(
                targetState = state.title,
                label = "Title Animation",
            ) {
                Text(
                    text = it ?: resources.getString(R.string.title_default_top_app_bar),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
    )
}

@Preview
@Composable
private fun PreviewLight() {
    VglsMaterial {
        PreviewContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    VglsMaterial {
        PreviewContent()
    }
}

@Composable
private fun PreviewContent() {
    RemasterTopBar(
        TopBarState(
            title = "VGLeadSheets",
            subtitle = "A Cool App",
        ),
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    )
}
