package com.vgleadsheets.remaster.games.detail

import android.app.Activity
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

@AssistedFactory
internal interface Factory {
    fun create(
        gameId: Long,
        navigateTo: (String) -> Unit
    ): ViewModelImpl
}

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface Provider {
    fun gameDetailViewModelFactory(): Factory
}

@Suppress("UNCHECKED_CAST")
internal fun provideFactory(
    assistedFactory: Factory,
    gameId: Long,
    navigateTo: (String) -> Unit
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(
            gameId,
            navigateTo,
        ) as T
    }
}

@Composable
fun gameDetailViewModel(
    gameId: Long,
    navigateTo: (String) -> Unit,
): ViewModelImpl {
    val activity = LocalContext.current as Activity
    val entryPoint = EntryPointAccessors.fromActivity(activity, Provider::class.java)
    val factory = entryPoint.gameDetailViewModelFactory()

    return viewModel(
        factory = provideFactory(
            factory,
            gameId,
            navigateTo
        )
    )
}
