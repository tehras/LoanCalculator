package com.koshkin.loancaluclator.loancalculator.fragments.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koshkin.loancaluclator.loancalculator.R
import java.io.Serializable

/**
 * Created by tehras on 7/17/16.
 *
 * Actionable Popup
 */
class SimpleActionableDialog : DialogFragment() {
    companion object Factory {
        val SUBMIT_ACTION_DEFAULT = "Submit"

        var messageText: CharSequence = ""
        var submitActionText: String = SUBMIT_ACTION_DEFAULT
        var displayCancel: Boolean = true
        private var callback: SimpleActionableCallback? = null

        val BUNDLE_MESSAGE_TEXT = "bundle_message_text"
        val BUNDLE_CALLBACK = "bundle_callback"
        val BUNDLE_ACTION_TEXT = "bundle_action_text"
        val BUNDLE_CANCEL_DISPLAY = "bundle_cancel_display"

        fun Builder(): SimpleBuilder {
            return SimpleBuilder()
        }

        class SimpleBuilder {
            fun setMessage(message: CharSequence): SimpleBuilder {
                messageText = message
                return this
            }

            fun displayCancel(display: Boolean): SimpleBuilder {
                displayCancel = display
                return this
            }

            fun callback(c: SimpleActionableCallback): SimpleBuilder {
                callback = c
                return this
            }

            fun create(): SimpleActionableDialog {
                val dialog = SimpleActionableDialog()
                val args = Bundle()
                if (messageText.isNullOrEmpty()) {
                    throw Exception("Message Text cannot be empty")
                }

                args.putCharSequence(BUNDLE_MESSAGE_TEXT, messageText)
                if (callback != null)
                    args.putSerializable(BUNDLE_CALLBACK, callback)
                args.putString(BUNDLE_ACTION_TEXT, submitActionText)
                args.putBoolean(BUNDLE_CANCEL_DISPLAY, displayCancel)

                dialog.arguments = args

                return dialog
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_dialog_simple_actionable, container, false)

        val actionButton = view.findViewById(R.id.action_button) as AppCompatButton
        val actionCancel = view.findViewById(R.id.action_cancel) as AppCompatButton
        val message = view.findViewById(R.id.message) as TextView

        if (arguments != null) {
            actionButton.text = arguments.getString(BUNDLE_ACTION_TEXT, SUBMIT_ACTION_DEFAULT)
            message.text = arguments.getCharSequence(BUNDLE_MESSAGE_TEXT)
            if (!arguments.getBoolean(BUNDLE_CANCEL_DISPLAY)) actionCancel.visibility = View.GONE
        }

        actionButton.setOnClickListener() {
            dismiss()
            if (callback != null) callback!!.submitAction()
        }
        actionCancel.setOnClickListener() { if (callback != null) callback!!.cancel(this) }

        return view
    }

    interface SimpleActionableCallback : Serializable {
        fun cancel(simpleActionableDialog: SimpleActionableDialog) {
            simpleActionableDialog.dismiss()
        }

        fun submitAction()
    }
}
