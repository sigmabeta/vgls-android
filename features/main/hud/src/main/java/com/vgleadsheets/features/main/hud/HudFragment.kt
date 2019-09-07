package com.vgleadsheets.features.main.hud

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import javax.inject.Inject

class HudFragment : VglsFragment() {
    @Inject
    lateinit var hudViewModelFactory: HudViewModel.Factory

    private val viewModel: HudViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
//                resources.getDimension(R.dimen.margin_large).toInt()
    }

    override fun getLayoutId() = R.layout.fragment_hud

    override fun invalidate() = withState(viewModel) {

    }
    
    companion object {
        fun newInstance(idArgs: IdArgs): HudFragment {
            val fragment = HudFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
