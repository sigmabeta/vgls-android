package com.vgleadsheets.scaffold

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.topbar.TopBarState

@OptIn(ExperimentalMaterial3Api::class)
data class TopBarConfig(
    val state: TopBarState,
    val behavior: TopAppBarScrollBehavior,
    val handleAction: (VglsAction) -> Unit,
)
