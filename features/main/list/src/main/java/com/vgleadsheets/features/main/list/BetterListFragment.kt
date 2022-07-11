package com.vgleadsheets.features.main.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.databinding.FragmentListBinding
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.tracking.api.InvalidateInfo
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForHeight
import com.vgleadsheets.setListsSpecialInsets
import javax.inject.Inject
import kotlin.system.measureNanoTime
import timber.log.Timber

abstract class BetterListFragment<
    ContentType : ListContent,
    StateType : BetterCompositeState<ContentType>
    > : VglsFragment() {
    private var progress: Float? = null

    abstract val viewModel: MvRxViewModel<StateType>

    abstract fun generateListConfig(state: StateType, hudState: HudState): BetterListConfig

    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    protected val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

    private lateinit var screen: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screen = FragmentListBinding.bind(view)
        screen.backgroundStatusBar.setInsetListenerForHeight(Side.TOP)

        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt()

        val content = view.findViewById<RecyclerView>(R.id.list_content)
        content.adapter = adapter
        content.layoutManager = LinearLayoutManager(context)

        adapter.resources = resources

        content.setListsSpecialInsets(bottomOffset)

        hudViewModel.setPerfSelectedScreen(getPerfSpec())
        hudViewModel.dontAlwaysShowBack()
    }

    override fun onStart() {
        super.onStart()
        val progress = this.progress
        if (progress != null) {
            screen.moLayoutScreen.progress = progress
        }
    }

    override fun onStop() {
        super.onStop()
        progress = screen.moLayoutScreen.progress
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val progress = screen.moLayoutScreen.progress

        Timber.i("Saving progress: $progress")

        outState.putFloat(KEY_PROGRESS_HEADER_SHRINK, progress)
    }

    override fun invalidate() {
        withState(viewModel, hudViewModel) { state, hudState ->
            val invalidateStartNanos = System.nanoTime()
            val invalidateDurationNanos = measureNanoTime {
                val config = generateListConfig(state, hudState)
                val title = Title.model(
                    config.titleConfig.title,
                    config.titleConfig.subtitle,
                    config.titleConfig.onImageLoadSuccess,
                    config.titleConfig.onImageLoadFail,
                    resources,
                    config.titleConfig.onMenuButtonClick,
                    config.titleConfig.photoUrl,
                    config.titleConfig.placeholder,
                    config.titleConfig.shouldShow,
                    config.titleConfig.isLoading,
                    config.titleConfig.titleGenerator,
                )

                if (title is TitleListModel) {
                    screen.toBind = title
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

    companion object {
        const val KEY_PROGRESS_HEADER_SHRINK =
            "${BuildConfig.LIBRARY_PACKAGE_NAME}.header_shrink_progress"
    }
}
