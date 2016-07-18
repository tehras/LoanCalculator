package com.koshkin.loancaluclator.loancalculator.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koshkin.loancaluclator.loancalculator.R

/**
 * Created by tehras on 7/17/16.
 *
 * Simple Error Dialog
 */
class SimpleErrorDialog : DialogFragment() {


    companion object Factory {
        fun create(errorMessage: CharSequence): SimpleErrorDialog = create(errorMessage, DEFAULT_ERROR_MESSAGE)

        fun create(errorMessage: CharSequence, titleMessage: CharSequence): SimpleErrorDialog = create(errorMessage, titleMessage, true)

        val DEFAULT_ERROR_MESSAGE = "Sorry, there was an error"
        val BUNDLE_ERROR_MESSAGE = "error_message_bundle_key"
        val BUNDLE_ERROR_TITLE = "error_title_bundle_key"
        val BUNDLE_DISMISS = "error_dismiss_bundle_key"

        fun create(errorMessage: CharSequence, titleMessage: CharSequence, dismiss: Boolean): SimpleErrorDialog {
            val fragment = SimpleErrorDialog()

            val bundle = Bundle()
            bundle.putString(BUNDLE_ERROR_MESSAGE, errorMessage.toString())
            bundle.putString(BUNDLE_ERROR_TITLE, titleMessage.toString())
            bundle.putBoolean(BUNDLE_DISMISS, dismiss)

            fragment.arguments = bundle

            return fragment
        }
    }

    var errorMessage: String = ""
    var titleMessage: String = DEFAULT_ERROR_MESSAGE //default
    var dismiss: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            errorMessage = arguments.getString(BUNDLE_ERROR_MESSAGE)
            titleMessage = arguments.getString(BUNDLE_ERROR_TITLE, DEFAULT_ERROR_MESSAGE)
            dismiss = arguments.getBoolean(BUNDLE_DISMISS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_dialog_simple_dialog, container, false)

        (view.findViewById(R.id.simple_dialog_body) as TextView).text = errorMessage
        (view.findViewById(R.id.simple_dialog_title) as TextView).text = titleMessage

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)

        d.setCanceledOnTouchOutside(dismiss)

        return d
    }
}
