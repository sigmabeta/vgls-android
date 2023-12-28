package com.vgleadsheets.features.main.list

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.main.list.compose.GridScreen
import com.vgleadsheets.features.main.list.databinding.FragmentListComposeBinding
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.util.ListModelGenerator
import com.vgleadsheets.mvrx.VglsState
import com.vgleadsheets.nav.BackHandler
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.perf.tracking.common.InvalidateInfo
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ComposeGridFragment<StateType : VglsState> : VglsFragment() {
    abstract val viewModel: MavericksViewModel<StateType>

    abstract fun generateListConfig(state: StateType, navState: NavState): ListConfig

    @Inject
    lateinit var dispatchers: VglsDispatchers

    protected val navViewModel: NavViewModel by existingViewModel()

    protected open val alwaysShowBack = true

    private var progress: Float? = null

    private lateinit var screenCompose: FragmentListComposeBinding

    private var configGenerationJob: Job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCompose(view)

        if (alwaysShowBack) {
            navViewModel.alwaysShowBack()
        } else {
            navViewModel.dontAlwaysShowBack()
        }

        navViewModel.setPerfSelectedScreen(getPerfSpec())
    }

    override fun invalidate() {
        withState(viewModel, navViewModel) { state, hudState ->
            val invalidateStartNanos = System.nanoTime()
            val invalidateDurationNanos = measureNanoTime {
                val config = generateListConfig(state, hudState)
                val title = Title.model(
                    config.titleConfig.title,
                    config.titleConfig.subtitle,
                    hudState.alwaysShowBack,
                    config.titleConfig.onImageLoadSuccess,
                    config.titleConfig.onImageLoadFail,
                    resources,
                    { (activity as BackHandler).onAppBarButtonClick() },
                    config.titleConfig.photoUrl,
                    config.titleConfig.placeholder,
                    config.titleConfig.allowExpansion,
                    config.titleConfig.isLoading,
                    config.titleConfig.titleGenerator,
                )

                if (configGenerationJob.isActive) {
                    hatchet.i(
                        this.javaClass.simpleName,
                        "${this::class.simpleName}: Canceling previous config generation job."
                    )
                    configGenerationJob.cancel()
                }

                configGenerationJob = viewModel.viewModelScope.launch(dispatchers.computation) {
                    val listItems = ListModelGenerator.generateUsingConfig(config, resources)

                    withContext(dispatchers.main) {
                        renderContentInCompose(
                            title,
                            listItems
                        )
                    }
                }
            }

            perfTracker.reportInvalidate(
                InvalidateInfo(
                    invalidateStartNanos,
                    invalidateDurationNanos
                ),
                getPerfSpec()
            )

            logPerfStages(state)
        }
    }

    private fun setupCompose(view: View) {
        screenCompose = FragmentListComposeBinding.bind(view)
    }

    private fun renderContentInCompose(
        title: TitleListModel,
        listItems: List<ListModel>
    ) {
        screenCompose.composeContent.setContent {
            GridScreen(title, listItems)
        }
    }

    private fun logPerfStages(state: StateType) {
        if (state.isEmpty()) {
            perfTracker.cancel(getPerfSpec())
            return
        }

        if (state.isReady()) {
            perfTracker.onPartialContentLoad(getPerfSpec())
        }

        if (state.isFullyLoaded()) {
            perfTracker.onFullContentLoad(getPerfSpec())
        }
    }

    override fun getLayoutId() = R.layout.fragment_list_compose

    override fun getVglsFragmentTag() = this.javaClass.simpleName
}
