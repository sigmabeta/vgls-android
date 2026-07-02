package com.vgleadsheets.search

import androidx.compose.runtime.Composable
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun searchViewModel(
    textUpdater: (String) -> Unit,
): SearchViewModel = assistedMetroViewModel<SearchViewModel, SearchViewModel.Factory> {
    create(textUpdater)
}
