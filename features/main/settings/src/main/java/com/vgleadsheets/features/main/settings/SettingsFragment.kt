package com.vgleadsheets.features.main.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_settings.list_settings
import javax.inject.Inject

class SettingsFragment : VglsFragment() {
    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    private val viewModel: SettingsViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_settings.adapter = adapter
        list_settings.layoutManager = LinearLayoutManager(context)
        list_settings.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(viewModel) { state ->

    }

    override fun getLayoutId() = R.layout.fragment_settings

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        fun newInstance(idArgs: IdArgs): SettingsFragment {
            val fragment = SettingsFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
