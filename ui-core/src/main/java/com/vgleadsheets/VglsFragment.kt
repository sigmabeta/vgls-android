package com.vgleadsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.mainstate.MainActivityViewModel
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    @LayoutRes
    abstract fun getLayoutId(): Int

    protected val mainViewModel: MainActivityViewModel by activityViewModel()

    fun onBackPressed() {
        mainViewModel.onBackPressed()
    }

    @CallSuper
    override fun invalidate() = withState(mainViewModel) { state ->
        if (state.searchClicked) {
            showSearch()
        }

        if (state.hideSearch) {
            hideSearch()
        }

        if (state.popBackStack) {
            popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        subscribeToSearchClicks()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater
        .inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
    }

    protected open fun showSearch() {
        getFragmentRouter().showSearch()
        mainViewModel.onSearchLaunch()
    }

    private fun hideSearch() {
        getFragmentRouter().hideSearch()
        mainViewModel.onSearchHide()
    }

    private fun popBackStack() {
        getFragmentRouter().popBackStack()
        mainViewModel.onBackStackPop()
    }

    protected fun showError(message: String, action: View.OnClickListener? = null, actionLabel: Int = 0) {
        Timber.e("Error getting sheets: $message")
        showSnackbar(message, action, actionLabel)
    }

    protected fun showSnackbar(message: String, action: View.OnClickListener? = null, actionLabel: Int = 0) {
        val toplevel = view?.parent as? CoordinatorLayout ?: view ?: return
        val snackbar = Snackbar.make(toplevel, message, Snackbar.LENGTH_LONG)

        if (action != null && actionLabel > 0) {
            snackbar.setAction(actionLabel, action)
        }

        snackbar.show()
    }

    protected fun getFragmentRouter() = (activity as FragmentRouter)

    private fun subscribeToSearchClicks() {
        mainViewModel.subscribeToSearchClicks(getFragmentRouter())
    }
}
