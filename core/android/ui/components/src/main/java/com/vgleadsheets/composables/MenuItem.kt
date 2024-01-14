package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.ui.themes.VglsMaterial

@OptIn(ExperimentalTextApi::class)
@Composable
fun MenuItem(
    model: MenuItemListModel,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_small)
            )
            .clickable(
                onClick = model.onClick,
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val color = if (model.selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Icon(
            painterResource(id = model.iconId),
            tint = color,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp)
                .padding(vertical = 12.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (model.selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
        )

        Text(
            text = model.caption ?: "",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily(
                    Font(DeviceFontFamilyName("sans-serif-condensed")),
                )
            ),
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 8.dp
                )
        )

    }
}

@Preview
@Composable
private fun Selected() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Preview
@Composable
private fun NotSelected() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(false)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectedDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotSelectedDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(false)
        }
    }
}

@Composable
private fun Sample(selected: Boolean) {
    MenuItem(
        MenuItemListModel(
            "Check for updates...",
            "Last updated Feb 3, 1963",
            com.vgleadsheets.ui.icons.R.drawable.ic_refresh_24dp,
            {},
            selected
        ),
        Modifier
    )
}

