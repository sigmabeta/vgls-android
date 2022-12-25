package com.vgleadsheets.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.MenuEmptyStateListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.themes.VglsMaterialMenu


@Composable
fun EmptyListIndicator(
    model: EmptyStateListModel,
) {
    EmptyListIndicator(
        explanation = model.explanation,
        iconId = model.iconId,
        showCrossOut = model.showCrossOut,
        menu = false
    )
}

@Composable
fun EmptyListIndicator(
    model: MenuEmptyStateListModel,
) {
    EmptyListIndicator(
        explanation = model.explanation,
        iconId = model.iconId,
        showCrossOut = model.showCrossOut,
        menu = true
    )
}

@Composable
private fun EmptyListIndicator(
    explanation: String,
    iconId: Int,
    showCrossOut: Boolean,
    menu: Boolean
) {
    val color = if (menu) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 8.dp
                )
        ) {
            val crossOutResource = if (menu) {
                R.drawable.ic_cross_out_menu_24dp
            } else {
                R.drawable.ic_cross_out_24dp
            }

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(96.dp)
            )

            if (showCrossOut) {
                Icon(
                    painter = painterResource(id = crossOutResource),
                    tint = Color.Unspecified,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                )
            }
        }

        Text(
            text = explanation,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun NotMenu() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleNotMenu()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NotMenuDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleNotMenu()
        }
    }
}

@Preview
@Composable
private fun Menu() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleMenu()
        }
    }
}

@Composable
private fun SampleNotMenu() {
    EmptyListIndicator(
        EmptyStateListModel(
            R.drawable.ic_album_24dp,
            "It's all part of the protocol, innit?",
            showCrossOut = true
        )
    )
}

@Composable
private fun SampleMenu() {
    EmptyListIndicator(
        MenuEmptyStateListModel(
            R.drawable.ic_person_24dp,
            "You hear that, Noah? Lanz wants something a little meatier.",
            showCrossOut = true
        )
    )
}

