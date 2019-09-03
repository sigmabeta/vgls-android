package com.vgleadsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.airbnb.mvrx.BaseMvRxFragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    private val disposables = CompositeDisposable()

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        subscribeToSearchClicks()
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater
        .inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
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
        val searchClicks = getFragmentRouter()
            .searchClicks()
            .subscribe {
                showSearch()
            }

        disposables.add(searchClicks)
    }

    private fun showSearch() {
        getFragmentRouter().showSearch()
    }
}
