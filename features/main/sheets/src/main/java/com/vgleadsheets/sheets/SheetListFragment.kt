package com.vgleadsheets.sheets

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.vgleadsheets.IdArgs

class SheetListFragment: BaseMvRxFragment() {

    override fun invalidate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(idArgs: IdArgs): SheetListFragment {
            val fragment = SheetListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}