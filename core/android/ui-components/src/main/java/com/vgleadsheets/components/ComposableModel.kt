package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ComposableModel {
    @Composable fun Content(modifier: Modifier)

    val dataId: Long

    val layoutId: Int
}