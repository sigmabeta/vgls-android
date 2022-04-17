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
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions")
abstract class VglsFragment : BaseMvRxFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var perfTracker: PerfTracker

    @JvmField
    @field:[Inject Named("RunningTest")]
    var isRunningTest: Boolean = false

    protected val idArgs: IdArgs by args()

    private var perfTrackingStarted = false

    private val targetTimes by lazy {
        hashMapOf(
            PerfStage.VIEW_CREATED.toString() to TARGET_VIEW_CREATED_MS,
            PerfStage.TITLE_LOADED.toString() to TARGET_TITLE_LOADED_MS,
            PerfStage.TRANSITION_START.toString() to TARGET_TRANSITION_START_MS,
            PerfStage.PARTIAL_CONTENT_LOAD.toString() to TARGET_PARTIAL_LOAD_MS,
            PerfStage.FULL_CONTENT_LOAD.toString() to getFullLoadTargetTime(),
            "completion" to TARGET_COMPLETION_MS
        )
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getVglsFragmentTag(): String

    abstract fun getTrackingScreen(): TrackingScreen

    abstract fun getFullLoadTargetTime(): Long

    open fun getDetails() = getArgs()?.id?.toString() ?: ""

    open fun getPerfScreenName(): String {
        val details = getDetails()

        return if (details.isNotEmpty()) {
            "${getTrackingScreen()}:$details"
        } else {
            getTrackingScreen().toString()
        }
    }

    @Suppress("SwallowedException")
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

    open fun getPerfTrackingMinScreenHeight() = 580

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    @SuppressWarnings("ReturnCount")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (disablePerfTracking() || isRunningTest) {
            Timber.d("Not starting perf tracker: Perf tracking is disabled for screen ${getPerfScreenName()}.")
            return
        }

        if (savedInstanceState != null) {
            Timber.i("Not starting perf tracker: Screen ${getPerfScreenName()} was recreated.")
            return
        }

        val displayMetrics = resources.displayMetrics
        val heightPixels = displayMetrics.heightPixels
        val heightDp = heightPixels.pxToDp(displayMetrics)
        val minHeightDp = getPerfTrackingMinScreenHeight()

        if (heightDp < minHeightDp) {
            Timber.i(
                "Not starting perf tracker: Screen height $heightDp dp too small " +
                    "for screen ${getPerfScreenName()} (min height $minHeightDp dp)."
            )
            return
        }

        perfTracker.start(getPerfScreenName(), targetTimes)
        perfTrackingStarted = true
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
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onStop() {
        super.onStop()
        if (perfTrackingStarted) {
            perfTracker.cancel(getPerfScreenName())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (perfTrackingStarted) {
            perfTracker.clear(getPerfScreenName())
            perfTrackingStarted = false
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

    companion object {
        private const val MS_PER_FRAME = 16.666667f

        private const val TARGET_VIEW_CREATED_FRAMES = 4
        private const val TARGET_TITLE_LOADED_FRAMES = 6
        private const val TARGET_TRANSITION_START_FRAMES = 20
        private const val TARGET_PARTIAL_LOAD_FRAMES = 90

        private const val TARGET_VIEW_CREATED_MS = (MS_PER_FRAME * TARGET_VIEW_CREATED_FRAMES).toLong()
        private const val TARGET_TITLE_LOADED_MS = (MS_PER_FRAME * TARGET_TITLE_LOADED_FRAMES).toLong()
        private const val TARGET_TRANSITION_START_MS = (MS_PER_FRAME * TARGET_TRANSITION_START_FRAMES).toLong()
        private const val TARGET_PARTIAL_LOAD_MS = (MS_PER_FRAME * TARGET_PARTIAL_LOAD_FRAMES).toLong()

        private const val TARGET_COMPLETION_MS = 5000L
    }
}
