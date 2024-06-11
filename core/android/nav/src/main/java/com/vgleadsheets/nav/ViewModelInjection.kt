package com.vgleadsheets.nav

import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope

@AssistedFactory
internal interface Factory {
    fun create(
        snackbarScope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
    ): NavViewModel
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface Provider {
    fun navViewModelFactory(): Factory
}

@Suppress("UNCHECKED_CAST")
internal fun provideFactory(
    assistedFactory: Factory,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(
            snackbarScope,
            snackbarHostState
        ) as T
    }
}

@Composable
fun navViewModel(
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
): NavViewModel {
    val activity = LocalContext.current as Activity
    val entryPoint = EntryPointAccessors.fromActivity(activity, Provider::class.java)
    val factory = entryPoint.navViewModelFactory()

    return viewModel(
        factory = provideFactory(
            factory,
            snackbarScope,
            snackbarHostState
        )
    )
}
