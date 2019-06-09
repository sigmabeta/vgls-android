package com.vgleadsheets.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxActivity
import com.airbnb.mvrx.withState
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.IdArgs
import com.vgleadsheets.games.GameListFragment
import com.vgleadsheets.sheets.SheetListFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseMvRxActivity(), HasSupportFragmentInjector, FragmentRouter {
    @Inject lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        if (savedInstanceState == null) {

        }
    }

    override fun supportFragmentInjector() = dispatchingFragmentInjector

    override fun showGameList() {
        // TODO Clear back stack before doing this.
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, GameListFragment.newInstance())
            .commit()
    }

    override fun showSheetListForGame(gameId: Long) = showFragmentSimple(SheetListFragment.newInstance(IdArgs(gameId)))
    
    private fun showFragmentSimple(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}