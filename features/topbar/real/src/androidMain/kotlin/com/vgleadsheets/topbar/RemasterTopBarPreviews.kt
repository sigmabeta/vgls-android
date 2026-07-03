package com.vgleadsheets.topbar

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.strings.text
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.TitleBarModel

@Preview
@Composable
private fun PreviewLight() {
    AppTheme {
        PreviewContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
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

