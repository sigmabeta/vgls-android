@file:OptIn(ExperimentalMaterial3Api::class)

package com.vgleadsheets.topbar

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.TitleBarModel
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
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        scrollBehavior = scrollBehavior,
        title = {
            Column {
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

                val subtitle = state.model.subtitle
                if (subtitle != null) {
                    Crossfade(
                        targetState = subtitle,
                        label = "Subtitle Animation",
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = TextUnit(12.0f, TextUnitType.Sp)
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        },
        navigationIcon = {
            val (vector, stringId, action) = if (state.model.shouldShowBack) {
                Triple(Icons.AutoMirrored.Default.ArrowBack, com.vgleadsheets.ui.strings.R.string.cont_desc_app_back, TopBarAction.AppBack)
            } else {
                Triple(Icons.Default.Menu, com.vgleadsheets.ui.strings.R.string.cont_desc_app_menu, TopBarAction.Menu)
            }

            IconButton(
                modifier = Modifier,
                onClick = { handleAction(action) }
            ) {
                Icon(
                    imageVector = vector,
                    contentDescription = resources.getString(stringId),
                )
            }
        },
        actions = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .clip(CircleShape)
                    .clickable { handleAction(TopBarAction.OpenPartPicker) }
            ) {
                Crossfade(
                    targetState = state.selectedPart,
                    label = "Title Animation",
                ) {
                    Text(
                        text = it ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = TextUnit(16.0f, TextUnitType.Sp)
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
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

    val collapsedTBState = rememberTopAppBarState(
        initialHeightOffsetLimit = -200.0f,
        initialContentOffset = -200.0f
    )
    val expandedTBState = rememberTopAppBarState(
        initialHeightOffset = 0.0f,
        initialHeightOffsetLimit = 0.0f,
    )

    Column {
        RemasterTopBar(
            state = TopBarState(
                model = TitleBarModel(
                    title = "$title - collapsed",
                    subtitle = subtitle,
                )
            ),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(collapsedTBState),
            handleAction = {},
        )
        RemasterTopBar(
            state = TopBarState(
                model = TitleBarModel(
                    title = "$title - expanded",
                    subtitle = subtitle,
                )
            ),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(expandedTBState),
            handleAction = {},
        )
    }
}
