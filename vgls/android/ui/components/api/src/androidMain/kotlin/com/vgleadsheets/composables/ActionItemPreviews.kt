package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.PreviewActionSink
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.CtaListModel
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import net.sigmabeta.sage.ui.vector

@Preview
@Composable
private fun Selected() {
    AppTheme {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
private fun NotMenu() {
    AppThemeMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    ActionItem(
        CtaListModel(
            Icon.FavoriteEmpty,
            "Find a path to the future",
            SageAction.Noop,
        ),
        PreviewActionSink { },
        padding = PaddingValues(horizontal = Dimensions.marginMedium),
        modifier = Modifier
    )
}
