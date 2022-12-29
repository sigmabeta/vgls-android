package com.vgleadsheets.components

import androidx.compose.runtime.Composable

interface ComposableModel {
    @Composable fun Content()

    val dataId: Long

    val layoutId: Int
}
