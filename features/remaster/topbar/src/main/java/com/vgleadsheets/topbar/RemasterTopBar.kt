package com.vgleadsheets.topbar

import android.content.res.Configuration
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.ui.themes.VglsMaterial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterTopBar(state: TopBarState) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleLarge,
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
    RemasterTopBar(
        TopBarState(
            title = "VGLeadSheets",
            subtitle = "A Cool App",
        )
    )
}
