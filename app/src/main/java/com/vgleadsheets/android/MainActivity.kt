package com.vgleadsheets.android

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.repository.RealRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class MainActivity : BaseMvRxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, GameListFragment())
                .commit()
        }

        val repository = RealRepository()

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