package com.vgleadsheets.features.main.jams

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import javax.inject.Inject

class JamListFragment : VglsFragment() {
    @Inject
    lateinit var jamListViewModelFactory: JamListViewModel.Factory

    private val viewModel: JamListViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
//                resources.getDimension(R.dimen.margin_large).toInt()
//        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
//                resources.getDimension(R.dimen.margin_medium).toInt()
    }

    override fun invalidate() = Unit/* withState(viewModel) { state ->

    }*/

    override fun getLayoutId() = R.layout.fragment_jam_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        fun newInstance(idArgs: IdArgs): JamListFragment {
            val fragment = JamListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
