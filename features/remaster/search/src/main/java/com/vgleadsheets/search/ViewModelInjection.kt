package com.vgleadsheets.search

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@AssistedFactory
internal interface Factory {
    fun create(
        @Assisted("textUpdater") textUpdater: (String) -> Unit,
    ): SearchViewModel
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface Provider {
    fun searchViewModelFactory(): Factory
}

@Suppress("UNCHECKED_CAST")
internal fun provideFactory(
    assistedFactory: Factory,
    textUpdater: (String) -> Unit,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(
            textUpdater
        ) as T
    }
}

@Composable
fun searchViewModel(
    textUpdater: (String) -> Unit,
): SearchViewModel {
    val activity = LocalContext.current as Activity
    val entryPoint = EntryPointAccessors.fromActivity(activity, Provider::class.java)
    val factory = entryPoint.searchViewModelFactory()

    return viewModel(
        factory = provideFactory(
            factory,
            textUpdater
        )
    )
}
