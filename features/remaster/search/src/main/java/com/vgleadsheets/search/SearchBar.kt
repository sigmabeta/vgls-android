package com.vgleadsheets.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.subs.MenuActionIcon
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun SearchBar(
    actionSink: ActionSink,
    modifier: Modifier,
    initialText: String = "",
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(com.vgleadsheets.ui.core.R.dimen.margin_side))
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(32.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuActionIcon(
                iconId = com.vgleadsheets.ui.icons.R.drawable.ic_arrow_back_black_24dp,
                onClick = { actionSink.sendAction(VglsAction.AppBack) }
            )

            var text by rememberSaveable { mutableStateOf(initialText) }

            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(vertical = 4.dp),
            ) {
                BasicTextField(
                    value = text,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    onValueChange = {
                        text = it
                        actionSink.sendAction(VglsAction.SearchQuerytEntered(it))
                    },
                )

                if (text.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.hint_search),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    return@Row
                }
            }

            MenuActionIcon(
                onClick = {
                    text = ""
                    actionSink.sendAction(VglsAction.SearchClearClicked)
                },
                iconId = com.vgleadsheets.ui.icons.R.drawable.ic_clear_black_24dp
            )
        }
    }
}

@Preview
@Composable
private fun EmptyState() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleEmpty()
        }
    }
}

@Preview
@Composable
private fun TextEntered() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleText()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun EmptyStateDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleEmpty()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TextEnteredDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleText()
        }
    }
}

@Composable
private fun SampleEmpty() {
    SearchBar(
        { },
        Modifier,
    )
}

@Composable
private fun SampleText() {
    SearchBar(
        { },
        Modifier,
        initialText = "Xenoblade Chronicles 3: Future Redeemed"
    )
}
