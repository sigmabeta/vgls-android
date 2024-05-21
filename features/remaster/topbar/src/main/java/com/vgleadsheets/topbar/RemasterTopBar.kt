@file:OptIn(ExperimentalMaterial3Api::class)

package com.vgleadsheets.topbar

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.composables.NameCaptionListItem
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun RemasterTopBar(
    state: TopBarState,
    scrollBehavior: TopAppBarScrollBehavior,
    handleAction: (VglsAction) -> Unit,
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
                targetState = state.model.title,
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
        navigationIcon = {
            val (vector, stringId, action) = if (state.model.shouldShowBack) {
                Triple(Icons.AutoMirrored.Default.ArrowBack, com.vgleadsheets.ui.strings.R.string.cont_desc_app_back, VglsAction.AppBack)
            } else {
                Triple(Icons.Default.Menu, com.vgleadsheets.ui.strings.R.string.cont_desc_app_menu, VglsAction.Menu)
            }

            IconButton(
                onClick = { handleAction(action) }
            ) {
                Icon(
                    imageVector = vector,
                    contentDescription = resources.getString(stringId),
                    modifier = Modifier
                )
            }
        },
        actions = {
            Text(
                text = "Bb",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
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
    val title = "VGLeadSheets"
    val subtitle = "A cool app"

    val collapsedTBState = rememberTopAppBarState()
    val modifier = Modifier

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(collapsedTBState)
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            RemasterTopBar(
                TopBarState(
                    model = TitleBarModel(
                        title = "$title - collapsed",
                        subtitle = subtitle,
                    )
                ),
                scrollBehavior
            ) { }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(innerPadding),
        ) {
            repeat(100) { index ->
                NameCaptionListItem(
                    model = NameCaptionListModel(
                        dataId = 1_000L + index,
                        name = "Item #$index",
                        caption = "Details about this object",
                        clickAction = VglsAction.Noop
                    ),
                    modifier = Modifier,
                    actionHandler = { }
                )
            }
        }
    }
}
