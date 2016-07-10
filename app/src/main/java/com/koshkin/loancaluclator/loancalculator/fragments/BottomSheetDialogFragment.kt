package com.koshkin.loancaluclator.loancalculator.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.View
import java.io.Serializable

/**
 * Created by koshkin on 7/4/16.
 *
 * Bottom Sheet Layout
 */
class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    val TAG = "BottomSheetDialogFragment"

    var childView: Int = 0
    var bottomSheetCallback: BottomSheetViewCallback? = null

    fun addView(view: Int, bottomSheetCallback: BottomSheetViewCallback): com.koshkin.loancaluclator.loancalculator.fragments.BottomSheetDialogFragment {
        this.childView = view
        this.bottomSheetCallback = bottomSheetCallback

        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            childView = savedInstanceState.getInt(KEY_RES)
            bottomSheetCallback = savedInstanceState.getSerializable(KEY_CALLBACK) as BottomSheetViewCallback?
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, childView, null)

        if (bottomSheetCallback != null)
            (bottomSheetCallback as BottomSheetViewCallback).manageView(view)

        dialog.setContentView(view)
        val behavior = BottomSheetBehavior.from(view.parent as View)

        view.post {
            behavior.peekHeight = view.measuredHeight
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    private val KEY_RES: String = "key_res_string"
    private val KEY_CALLBACK: String = "key_callback_string"

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(KEY_RES, childView)
        outState?.putSerializable(KEY_CALLBACK, bottomSheetCallback)

        super.onSaveInstanceState(outState)
    }

    interface BottomSheetViewCallback : Serializable {
        fun manageView(view: View)
    }
}