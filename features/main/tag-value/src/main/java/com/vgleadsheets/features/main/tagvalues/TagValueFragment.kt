package com.vgleadsheets.features.main.tagvalues

import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.ComposeListFragment
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class TagValueFragment : ComposeListFragment<TagValueState>() {
    @Inject
    lateinit var viewModelFactory: TagValueViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE

    override fun getPerfSpec() = PerfSpec.TAG_VALUE

    override val viewModel: TagValueViewModel by fragmentViewModel()

    override fun generateListConfig(state: TagValueState, navState: NavState) = Config(
        state,
        navState,
        Clicks(
            getFragmentRouter()
        ),
        perfTracker,
        getPerfSpec(),
        resources,
        hatchet
    )

    companion object {
        const val LOAD_OPERATION = "loadTagValue"

        fun newInstance(idArgs: IdArgs): TagValueFragment {
            val fragment = TagValueFragment()

            val args = Bundle()
            args.putParcelable(Mavericks.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
