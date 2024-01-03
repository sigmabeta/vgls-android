package com.vgleadsheets.remaster.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.composables.NameCaptionListItem

@Composable
fun GameScreen(
    state: GameState
) {
    NameCaptionListItem(
        model = NameCaptionListModel(
            dataId = 1234L,
            name = state.title,
            caption = state.subtitle,
            onClick = { }
        ),
        modifier = Modifier
    )
}
