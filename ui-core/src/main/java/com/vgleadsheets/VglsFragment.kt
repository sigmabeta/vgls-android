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
import com.vgleadsheets.perf.tracking.common.LoadStatus
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.perf.view.common.PerfView
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var perfTracker: PerfTracker

    protected val idArgs: IdArgs by args()

    private var prevLoadStatus: LoadStatus? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getVglsFragmentTag(): String

    abstract fun getTrackingScreen(): TrackingScreen

    abstract fun getPerfView(): PerfView?

    abstract fun tellViewmodelPerfCancelled()

    open fun getDetails() = getArgs()?.id?.toString() ?: ""

    open fun getPerfScreenName(): String {
        val details = getDetails()

        return if (details.isNotEmpty()) {
            "${getTrackingScreen()}:$details"
        } else {
            getTrackingScreen().toString()
        }
    }

    open fun getArgs(): IdArgs? {
        return try {
            idArgs
        } catch (ex: IllegalArgumentException) {
            null
        } catch (ex: ClassCastException) {
            null
        }
    }

    open fun onBackPress() = false

    open fun disablePerfTracking() = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (disablePerfTracking()) {
            prevLoadStatus = LoadStatus(cancelled = true)
            Timber.d("Not starting perf tracker: Perf tracking is disabled for screen ${getPerfScreenName()}.")
            return
        }

        if (savedInstanceState != null) {
            prevLoadStatus = LoadStatus(cancelled = true)
            Timber.i("Not starting perf tracker: Screen ${getPerfScreenName()} was recreated.")
            return
        }

        perfTracker.start(getPerfScreenName())
        getPerfView()?.start(getPerfScreenName())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater
        .inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!disablePerfTracking()) {
            perfTracker.onViewCreated(getPerfScreenName())
            getPerfView()?.viewCreated(getPerfScreenName())
        }

        ViewCompat.requestApplyInsets(view)
    }


    override fun onStop() {
        super.onStop()
        if (!disablePerfTracking()) {
            if (prevLoadStatus?.isLoadComplete() == true) {
                return
            }

            perfTracker.cancel(getPerfScreenName())
            getPerfView()?.cancelled(getPerfScreenName())
            tellViewmodelPerfCancelled()
        }
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

    protected fun onLoadStatusUpdate(status: LoadStatus) {
        if (prevLoadStatus?.isLoadComplete() == true) {
            return
        }

        val screenName = getPerfScreenName()
        val perfView = getPerfView()

        if (prevLoadStatus?.cancelled != true && (status.cancelled || status.loadFailed)) {
            perfTracker.cancel(screenName)
            perfView?.cancelled(screenName)
            getPerfView()?.cancelled(getPerfScreenName())
            prevLoadStatus = status
            return
        }

        if (status.titleLoaded && prevLoadStatus?.titleLoaded != true) {
            perfTracker.onTitleLoaded(screenName)
            perfView?.titleLoaded(screenName)
            getPerfView()?.titleLoaded(getPerfScreenName())  }

        if (status.transitionStarted && prevLoadStatus?.transitionStarted != true) {
            perfTracker.onTransitionStarted(screenName)
            perfView?.transitionStarted(screenName)
            getPerfView()?.transitionStarted(getPerfScreenName())
        }

        if (status.contentPartiallyLoaded && prevLoadStatus?.contentPartiallyLoaded != true) {
            perfTracker.onPartialContentLoad(screenName)
            perfView?.partialContentLoaded(screenName)
            getPerfView()?.partialContentLoaded(getPerfScreenName())  }

        if (status.contentFullyLoaded && prevLoadStatus?.contentFullyLoaded != true) {
            perfTracker.onFullContentLoad(screenName)
            perfView?.fullContentLoaded(screenName)
            getPerfView()?.fullContentLoaded(getPerfScreenName())}

        if (status.isLoadComplete()) {
            Timber.d("Load complete!")
            perfView?.completed(screenName)
            getPerfView()?.completed(getPerfScreenName())
        }

        prevLoadStatus = status
    }
}
