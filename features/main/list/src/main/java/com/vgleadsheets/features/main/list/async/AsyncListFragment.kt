package com.vgleadsheets.features.main.list.async

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.R
import com.vgleadsheets.perf.tracking.common.LoadStatus
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setListsSpecialInsets
import com.vgleadsheets.tabletSetListsSpecialInsets
import kotlinx.android.synthetic.main.fragment_list.list_content
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KProperty1

abstract class AsyncListFragment<DataType : ListData, StateType : AsyncListState<DataType>> : VglsFragment() {
    abstract val viewModel: AsyncListViewModel<DataType, StateType>

    abstract val loadStatusProperty: KProperty1<StateType, LoadStatus>

    abstract fun subscribeToViewEvents()

    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    protected val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

    private var prevLoadStatus: LoadStatus? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = context!!.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt()

        list_content.adapter = adapter
        list_content.layoutManager = LinearLayoutManager(context)

        if (dpWidth > WIDTH_THRESHOLD_TABLET) {
            list_content.tabletSetListsSpecialInsets(topOffset, bottomOffset)
        } else {
            list_content.setListsSpecialInsets(topOffset, bottomOffset)
        }

        hudViewModel.alwaysShowBack()

        hudViewModel.selectSubscribe(HudState::digest) {
            viewModel.onDigestUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::updateTime) {
            viewModel.onTimeUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::parts) { parts ->
            viewModel.onSelectedPartUpdate(parts.firstOrNull { it.selected })
        }

        setupPerfReporting()
        subscribeToViewEvents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        adapter.submitList(state.listModels)
    }

    override fun getLayoutId() = R.layout.fragment_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun tellViewmodelPerfCancelled() {
        viewModel.cancelPerf()
    }

    private fun setupPerfReporting() {
        viewModel.selectSubscribe(loadStatusProperty, deliveryMode = UniqueOnly("perf")) { status ->
            Timber.v("Received new loadStatus: $status")

            if (status.cancelled || status.loadFailed) {
                perfTracker.cancel(getPerfScreenName())
                prevLoadStatus = status
                return@selectSubscribe
            }

            if (status.titleLoaded && prevLoadStatus?.titleLoaded != true) {
                perfTracker.onTitleLoaded(getPerfScreenName())
            }

            if (status.transitionStarted && prevLoadStatus?.transitionStarted != true) {
                perfTracker.onTransitionStarted(getPerfScreenName())
            }

            if (status.contentPartiallyLoaded && prevLoadStatus?.contentPartiallyLoaded != true) {
                perfTracker.onPartialContentLoad(getPerfScreenName())
            }

            if (status.contentFullyLoaded && prevLoadStatus?.contentFullyLoaded != true) {
                perfTracker.onFullContentLoad(getPerfScreenName())
            }

            prevLoadStatus = status
        }
    }

    companion object {
        const val WIDTH_THRESHOLD_TABLET = 500.0f
    }
}
