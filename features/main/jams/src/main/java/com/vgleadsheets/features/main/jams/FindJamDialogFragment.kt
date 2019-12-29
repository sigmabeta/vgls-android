package com.vgleadsheets.features.main.jams

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.Tracker
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_jam.button_cancel
import kotlinx.android.synthetic.main.fragment_find_jam.button_find
import kotlinx.android.synthetic.main.fragment_find_jam.edit_jam_name
import kotlinx.android.synthetic.main.fragment_find_jam.progress_loading
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class FindJamDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var repository: Repository

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_find_jam, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(view)

        button_cancel.setOnClickListener { dismiss() }
        button_find.setOnClickListener { onAddClicked() }
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun onAddClicked() {
        val jamName = edit_jam_name.text.toString()

        if (jamName.isNotBlank()) {
            findJam(jamName)
        } else {
            showError("Jam name can't be empty.")
        }
    }

    private fun findJam(jamName: String) {
        progress_loading.visibility = VISIBLE
        button_find.visibility = GONE
        val find = repository.refreshJamState(jamName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    progress_loading.visibility = GONE
                    button_find.visibility = VISIBLE

                    showSnackbar("Added jam ${it.name}")
                    dismiss()
                },
                {
                    progress_loading.visibility = GONE
                    button_find.visibility = VISIBLE

                    if (it is HttpException) {
                        if (it.code() == 404) {
                            showError(getString(R.string.error_could_not_find_jam))
                        } else {
                            showError(getString(R.string.error_network))
                        }
                    } else if (it is UnknownHostException) {
                        showError(getString(R.string.error_connection))
                    } else {
                        showError("Could not find Jam: ${it.message}")
                    }
                }
            )

        disposables.add(find)
    }

    private fun showError(message: String) {
        Timber.e("Displayed error: $message")
        tracker.logError(message)
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    protected fun showSnackbar(
        message: String,
        action: View.OnClickListener? = null,
        actionLabel: Int = 0,
        length: Int = Snackbar.LENGTH_LONG
    ): Snackbar {
        val toplevel = view?.parent as? CoordinatorLayout ?: view ?: throw IllegalStateException()
        val snackbar = Snackbar.make(toplevel, message, length)

        if (action != null && actionLabel > 0) {
            snackbar.setAction(actionLabel, action)
        }

        snackbar.show()
        return snackbar
    }

    companion object {
        fun newInstance() = FindJamDialogFragment()
    }
}