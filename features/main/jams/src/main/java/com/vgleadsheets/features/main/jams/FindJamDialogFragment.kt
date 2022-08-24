package com.vgleadsheets.features.main.jams

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vgleadsheets.insets.Insets
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.Tracker
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.inject.Inject
import retrofit2.HttpException
import timber.log.Timber

class FindJamDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var repository: Repository

    private lateinit var container_linear: LinearLayout

    private lateinit var buttonCancel: Button

    private lateinit var buttonFind: Button

    private lateinit var editJamName: EditText

    private lateinit var progressLoading: ProgressBar

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

        container_linear = view.findViewById(R.id.dialog_toplevel)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonFind = view.findViewById(R.id.button_find)
        editJamName = view.findViewById(R.id.edit_jam_name)
        progressLoading = view.findViewById(R.id.progress_loading)

        buttonCancel.setOnClickListener { dismiss() }
        buttonFind.setOnClickListener { onAddClicked() }
        editJamName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onAddClicked()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        val designBottomSheet = container_linear.parent
        val coordinator = designBottomSheet.parent
        val container = coordinator.parent

        Insets.setupRootViewForInsetAnimation(container as View)
        Insets.setupInsetAnimationFor(designBottomSheet as View)
        Insets.setupControlFocusForInsetAnimation(editJamName)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun onAddClicked() {
        val jamName = editJamName.text.toString()

        if (jamName.isNotBlank()) {
            findJam(jamName.lowercase())
        } else {
            showError("Jam name can't be empty.")
        }
    }

    private fun findJam(jamName: String) {
        progressLoading.visibility = VISIBLE
        buttonFind.visibility = GONE
        val find = repository.refreshJamState(jamName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    progressLoading.visibility = GONE
                    buttonFind.visibility = VISIBLE

                    dismiss()
                },
                {
                    progressLoading.visibility = GONE
                    buttonFind.visibility = VISIBLE

                    if (it is HttpException) {
                        if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                            showError(getString(R.string.error_could_not_find_jam))
                        } else {
                            showError(getString(R.string.error_network))
                        }
                    } else if (it is UnknownHostException) {
                        showError(getString(R.string.error_connection))
                    } else {
                        showError("Could not find Jam: ${it.message}")
                    }

                    Timber.e("Error finding Jam: ${it.message}")
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

    companion object {
        fun newInstance() = FindJamDialogFragment()
    }
}
