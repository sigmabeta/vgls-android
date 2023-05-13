package com.vgleadsheets.features.main.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.FragmentInterface
import com.vgleadsheets.IsComposeEnabled
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.databinding.FragmentListComposeBinding
import com.vgleadsheets.features.main.list.databinding.FragmentListRecyclerBinding
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.insets.Insetup
import com.vgleadsheets.perf.tracking.common.InvalidateInfo
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setListsSpecialInsets
import com.vgleadsheets.themes.VglsMaterial
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BetterListFragment<
    ContentType : ListContent,
    StateType : BetterCompositeState<ContentType>
    > : VglsFragment() {
    abstract val viewModel: MavericksViewModel<StateType>

    abstract fun generateListConfig(state: StateType, hudState: HudState): BetterListConfig

    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    @Inject
    lateinit var dispatchers: VglsDispatchers

    protected val hudViewModel: HudViewModel by existingViewModel()

    protected open val alwaysShowBack = true

    private lateinit var adapter: ComponentAdapter

    private var progress: Float? = null

    private lateinit var screenLegacy: FragmentListRecyclerBinding
    private lateinit var screenCompose: FragmentListComposeBinding

    private var configGenerationJob: Job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ComponentAdapter(getVglsFragmentTag(), hatchet)

        if (IsComposeEnabled.WELL_IS_IT) {
            setupCompose(view)
        } else {
            setupRecycler(view)
            setupAppBar()
            Insetup.setupRootViewForInsetAnimation(screenLegacy.root)
        }

        if (alwaysShowBack) {
            hudViewModel.alwaysShowBack()
        } else {
            hudViewModel.dontAlwaysShowBack()
        }

        hudViewModel.setPerfSelectedScreen(getPerfSpec())
    }

    override fun onStart() {
        super.onStart()
        val progress = this.progress
        if (progress != null && !IsComposeEnabled.WELL_IS_IT) {
            screenLegacy.moLayoutToolbar.progress = progress
        }
    }

    override fun onStop() {
        super.onStop()
        if (!IsComposeEnabled.WELL_IS_IT) {
            progress = screenLegacy.moLayoutToolbar.progress
        }
    }

    override fun invalidate() {
        withState(viewModel, hudViewModel) { state, hudState ->
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
                    { (activity as FragmentInterface).onAppBarButtonClick() },
                    config.titleConfig.photoUrl,
                    config.titleConfig.placeholder,
                    config.titleConfig.allowExpansion,
                    config.titleConfig.isLoading,
                    config.titleConfig.titleGenerator,
                )

                if (IsComposeEnabled.WELL_IS_IT) {
                    // Render title in compose lol
                } else {
                    screenLegacy.toBind = title

                    if (title.allowExpansion) {
                        screenLegacy.moLayoutToolbar.progress = 1.0f
                        screenLegacy.moLayoutToolbar.enableTransition(R.id.transition_scroll, false)
                    }
                }

                if (configGenerationJob.isActive) {
                    hatchet.i(
                        this.javaClass.simpleName,
                        "${this::class.simpleName}: Canceling previous config generation job."
                    )
                    configGenerationJob.cancel()
                }

                configGenerationJob = viewModel.viewModelScope.launch(dispatchers.computation) {
                    val listItems = BetterLists.generateList(config, resources)

                    withContext(dispatchers.main) {
                        if (IsComposeEnabled.WELL_IS_IT) {
                            renderContentInCompose(listItems)
                        } else {
                            renderContentInRecyclerView(listItems)
                        }
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

    private fun setupRecycler(view: View) {
        screenLegacy = FragmentListRecyclerBinding.bind(view)
        screenLegacy.listContent.adapter = adapter
        screenLegacy.listContent.layoutManager = LinearLayoutManager(context)

        adapter.resources = resources

        val bottomOffset =
            resources.getDimension(com.vgleadsheets.ui_core.R.dimen.height_bottom_sheet_peek)
                .toInt()
        screenLegacy.listContent.setListsSpecialInsets(bottomOffset)
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun renderContentInCompose(listItems: List<ListModel>) {
        screenCompose.composeContent.setContent {
            VglsMaterial {
                LazyColumn(
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    items(
                        items = listItems.toTypedArray(),
                        key = { it.dataId },
                        contentType = { it.layoutId }
                    ) {
                        it.Content(
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }

    private fun renderContentInRecyclerView(listItems: List<ListModel>) {
        adapter.submitList(
            listItems
        )
    }

    private fun setupAppBar() {
        screenLegacy.appBar.addOnOffsetChangedListener { appBar, verticalOffset ->
            val seekPosition = -verticalOffset / appBar.totalScrollRange.toFloat()
            screenLegacy.moLayoutToolbar.progress = seekPosition
        }

        val desiredToolbarHeight = screenLegacy.moLayoutToolbar.minHeight

        ViewCompat.setOnApplyWindowInsetsListener(screenLegacy.moLayoutToolbar) { _, insets: WindowInsetsCompat ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val insetTopHeight = systemBarInsets.top
            val collapsedToolbarHeight = desiredToolbarHeight + insetTopHeight

            screenLegacy.moLayoutToolbar.minimumHeight = collapsedToolbarHeight

            val bigConstraintSet = screenLegacy.moLayoutToolbar.getConstraintSet(R.id.big)
            val smallConstraintSet = screenLegacy.moLayoutToolbar.getConstraintSet(R.id.small)

            bigConstraintSet.setGuidelineBegin(R.id.guideline_inset, insetTopHeight)
            smallConstraintSet.setGuidelineEnd(R.id.guideline_inset, desiredToolbarHeight)
            smallConstraintSet.setGuidelineEnd(R.id.guideline_collapsed, collapsedToolbarHeight)

            insets
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

    override fun getLayoutId() = if (IsComposeEnabled.WELL_IS_IT) {
        R.layout.fragment_list_compose
    } else {
        R.layout.fragment_list_recycler
    }

    override fun getVglsFragmentTag() = this.javaClass.simpleName
}
