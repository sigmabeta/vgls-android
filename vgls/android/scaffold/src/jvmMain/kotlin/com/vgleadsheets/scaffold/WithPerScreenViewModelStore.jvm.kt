package com.vgleadsheets.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

/**
 * JVM/desktop actual — hangs a [ViewModelStore] off a Voyager [ScreenModel] keyed to [screen].
 * Voyager retains ScreenModels for as long as the screen is in the back stack and calls
 * [ScreenModel.onDispose] only when it's popped, so the store (and its VMs) survive navigating away
 * and back — matching Android's AndroidScreenLifecycleOwner. A plain composition `remember` would
 * drop it the moment the screen stopped being the top of the stack.
 */
@Composable
internal actual fun WithPerScreenViewModelStore(screen: Screen, content: @Composable () -> Unit) {
    val holder = screen.rememberScreenModel { ViewModelStoreHolder() }
    val owner = remember(holder) {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = holder.store
        }
    }
    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}

private class ViewModelStoreHolder : ScreenModel {
    val store = ViewModelStore()

    override fun onDispose() {
        store.clear()
    }
}
