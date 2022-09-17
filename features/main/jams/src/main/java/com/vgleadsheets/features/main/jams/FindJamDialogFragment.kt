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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vgleadsheets.insets.Insetup
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.Tracker
import dagger.android.support.AndroidSupportInjection
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class FindJamDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var repository: Repository

    private lateinit var containerLinear: LinearLayout

    private lateinit var buttonCancel: Button

    private lateinit var buttonFind: Button

    private lateinit var editJamName: EditText

    private lateinit var progressLoading: ProgressBar

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

        containerLinear = view.findViewById(R.id.dialog_toplevel)
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
        val designBottomSheet = containerLinear.parent
        val coordinator = designBottomSheet.parent
        val container = coordinator.parent

        Insetup.setupRootViewForInsetAnimation(container as View)
        Insetup.setupInsetAnimationFor(designBottomSheet as View)
        Insetup.setupControlFocusForInsetAnimation(editJamName)
    }

    private fun onAddClicked() {
        val jamName = editJamName.text.toString()

        if (jamName.isNotBlank()) {
            findJam(jamName.lowercase())
        } else {
            showError("Jam name can't be empty.")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun findJam(jamName: String) {
        progressLoading.visibility = VISIBLE
        buttonFind.visibility = GONE

        // TODO Not this
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.refreshJamState(jamName)

                progressLoading.visibility = GONE
                buttonFind.visibility = VISIBLE

                dismiss()
            } catch (it: Exception) {

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
        }
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
