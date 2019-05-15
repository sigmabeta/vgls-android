package com.vgleadsheets.android

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.repository.RealRepository
import com.vgleadsheets.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseMvRxActivity() {

    lateinit var repository: Repository
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as VglsApplication).appComponent.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, GameListFragment())
                .commit()
        }

        val disposable = repository.getGames()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.i("Received list of ${it.size} items.")
                },
                {
                    Timber.e("Error loading games: ${it.message}")
                })
    }
}