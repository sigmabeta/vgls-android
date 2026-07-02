package com.vgleadsheets.ui.viewer

import androidx.compose.runtime.Composable
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun viewerViewModel(
    idArg: Long,
    pageArg: Long,
): ViewerViewModel = assistedMetroViewModel<ViewerViewModel, ViewerViewModel.Factory> {
    create(idArg, pageArg)
}
