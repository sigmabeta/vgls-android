package com.vgleadsheets.features.main.about

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

class AboutFragment : VglsFragment() {
    @Inject
    lateinit var aboutViewModelFactory: AboutViewModel.Factory

    private val viewModel: AboutViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()
    }

    override fun invalidate() = withState(viewModel) { state ->

    }

    override fun getLayoutId() = R.layout.fragment_about

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        fun newInstance(idArgs: IdArgs): AboutFragment {
            val fragment = AboutFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
