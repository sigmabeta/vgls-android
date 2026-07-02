package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.previews.NotifConstants
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.NotifListModel

@Preview
@Composable
private fun Light() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Sample()
        }
    }
}

@Composable
@Suppress("MagicNumber", "MaxLineLength", "LongMethod")
private fun Sample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.marginSide)
    ) {
        NotifListItem(
            NotifListModel(
                1234L,
                "This App Is Cool",
                "Here's what you need to know about how cool this app is. I might render on two lines. Isn't that awesome?",
                "Find out more",
                SageAction.Noop,
                false
            ),
            PreviewActionSink { },
            modifier = Modifier
        )

        NotifListItem(
            NotifListModel(
                1234L,
                "Really Long Notif",
                "Here's what you need to know about how cool this app is. I might render on two lines. Or even on three. heck, we might do four. Sky's the limit. Isn't that awesome?",
                "That sure is long, all right",
                SageAction.Noop,
                false
            ),
            PreviewActionSink { },
            modifier = Modifier
        )

        NotifListItem(
            NotifListModel(
                1234L,
                "This Notif Has No Action",
                "Here's what you need to know about how cool this app is. I might render on two lines. Isn't that awesome?",
                "Find out more",
                null,
                false
            ),
            PreviewActionSink { },
            modifier = Modifier
        )

        NotifListItem(
            NotifListModel(
                1234L,
                "Situation Very Wrong",
                "Everything is broken!",
                "Fix it",
                SageAction.Noop,
                true
            ),
            PreviewActionSink { },
            modifier = Modifier
        )
    }
}
