package com.vgleadsheets.features.main.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.appbar.AppBarLayout
import com.vgleadsheets.FragmentInterface
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.databinding.FragmentListBinding
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.insets.Insetup
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.tracking.api.InvalidateInfo
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setListsSpecialInsets
import javax.inject.Inject
import kotlin.system.measureNanoTime

abstract class BetterListFragment<
    ContentType : ListContent,
    StateType : BetterCompositeState<ContentType>
    > : VglsFragment() {
    abstract val viewModel: MvRxViewModel<StateType>

    abstract fun generateListConfig(state: StateType, hudState: HudState): BetterListConfig

    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    protected val hudViewModel: HudViewModel by existingViewModel()

    protected open val alwaysShowBack = true

    private val adapter = ComponentAdapter()

    private var progress: Float? = null

    private lateinit var screen: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screen = FragmentListBinding.bind(view)
        screen.listContent.adapter = adapter
        screen.listContent.layoutManager = LinearLayoutManager(context)

        adapter.resources = resources

        setupAppBar()

        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt()
        screen.listContent.setListsSpecialInsets(bottomOffset)

        Insetup.setupRootViewForInsetAnimation(screen.root)

        hudViewModel.setPerfSelectedScreen(getPerfSpec())

        if (alwaysShowBack) {
            hudViewModel.alwaysShowBack()
        } else {
            hudViewModel.dontAlwaysShowBack()
        }
    }

    override fun onStart() {
        super.onStart()
        val progress = this.progress
        if (progress != null) {
            screen.moLayoutToolbar.progress = progress
        }
    }

    override fun onStop() {
        super.onStop()
        progress = screen.moLayoutToolbar.progress
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

                screen.toBind = title

                if (title.allowExpansion) {
                    screen.moLayoutToolbar.progress = 1.0f
                    screen.moLayoutToolbar.enableTransition(R.id.transition_scroll, false)
                }

                val listItems = BetterLists.generateList(config, resources)

                adapter.submitList(
                    listItems
                )
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

    private fun setupAppBar() {
        screen.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
                val seekPosition = -verticalOffset / appBar.totalScrollRange.toFloat()
                screen.moLayoutToolbar.progress = seekPosition
            }
        )

        val desiredToolbarHeight = screen.moLayoutToolbar.minHeight

        ViewCompat.setOnApplyWindowInsetsListener(screen.moLayoutToolbar) { _, insets: WindowInsetsCompat ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val insetTopHeight = systemBarInsets.top

            screen.moLayoutToolbar.minimumHeight = desiredToolbarHeight + insetTopHeight

            val startConstraintSet = screen.moLayoutToolbar.getConstraintSet(R.id.big)
            val endConstraintSet = screen.moLayoutToolbar.getConstraintSet(R.id.guideline_collapsed)

            startConstraintSet.setGuidelineBegin(R.id.guideline_inset, insetTopHeight)
            endConstraintSet.setGuidelineEnd(R.id.guideline_inset, desiredToolbarHeight)
            endConstraintSet.setGuidelineEnd(
                R.id.guideline_collapsed,
                desiredToolbarHeight + insetTopHeight
            )

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

    override fun getLayoutId() = R.layout.fragment_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName
}
