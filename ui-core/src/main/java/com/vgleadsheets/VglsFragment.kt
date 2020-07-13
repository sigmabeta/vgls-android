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
import com.airbnb.mvrx.args
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    @Inject
    lateinit var tracker: Tracker

    protected val idArgs: IdArgs by args()

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getVglsFragmentTag(): String

    abstract fun getTrackingScreen(): TrackingScreen

    open fun getArgs(): IdArgs? {
        return try {
            idArgs
        } catch (ex: IllegalArgumentException) {
            null
        }
    }

    open fun onBackPress() = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater
        .inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)
    }

    protected fun showError(
        error: Throwable,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0
    ) {
        val message = error.message ?: error::class.simpleName ?: "Unknown Error"
        showError(message, action, actionLabel)
    }

    protected fun showError(
        message: String,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0
    ) {
        Timber.e("Displayed error: $message")
        tracker.logError(message)
        showSnackbar(message, action, actionLabel)
    }

    protected fun showSnackbar(
        message: String,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0,
        length: Int = Snackbar.LENGTH_LONG
    ): Snackbar? {
        val toplevel = view?.parent as? CoordinatorLayout ?: view ?: return null
        val snackbar = Snackbar.make(toplevel, message, length)

        if (action != null && actionLabel > 0) {
            snackbar.setAction(actionLabel, action)
        }

        snackbar.show()
        return snackbar
    }

    protected fun getFragmentRouter() = (activity as FragmentRouter)
}
