package com.vgleadsheets.features.main.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setListsSpecialInsets
import com.vgleadsheets.tabletSetListsSpecialInsets
import kotlinx.android.synthetic.main.fragment_list.list_content
import javax.inject.Inject

abstract class ListFragment<DataType, StateType : ListState<DataType>> : VglsFragment() {
    abstract val viewModel: ListViewModel<DataType, StateType>

    abstract fun subscribeToViewEvents()

    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    protected val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

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

        hudViewModel.dontAlwaysShowBack()

        hudViewModel.selectSubscribe(HudState::digest) {
            viewModel.onDigestUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::updateTime) {
            viewModel.onTimeUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::selectedPart) { part ->
            viewModel.onSelectedPartUpdate(part)
        }

        subscribeToViewEvents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        adapter.submitList(state.listModels)
    }

    override fun getLayoutId() = R.layout.fragment_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        const val WIDTH_THRESHOLD_TABLET = 500.0f
    }
}
