package com.koshkin.loancaluclator.loancalculator

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.koshkin.loancaluclator.loancalculator.R.anim
import com.koshkin.loancaluclator.loancalculator.fragments.bottom_sheets.BottomSheetDialogFragment

/**
 * Created by koshkin on 7/4/16.
 *
 * Base Fragment
 */
open class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()
    }

    fun addBottomSheet(view: Int, callback: BottomSheetDialogFragment.BottomSheetViewCallback) {
        BottomSheetDialogFragment().addView(view, callback).show(activity.supportFragmentManager, "bottom_sheet_dialog")
    }

    fun startFragment(a: AppCompatActivity) {
        startFragment(a, false)
    }

    fun startFragment(a: AppCompatActivity, first: Boolean) {
        val tran = a.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_in_animation, anim.fragment_out_animation)
                .addToBackStack(this.javaClass.simpleName)

        if (!first)
            tran.replace(R.id.fragment_container, this)
        else tran.add(R.id.fragment_container, this)

        tran.commit()
    }

    private var dialog: ProgressDialog? = null

    fun hideProgressDialog() {
        if (dialog != null)
            dialog!!.dismiss()
    }

    fun showProgressDialog(loadingText: String) {
        dialog = ProgressDialog.show(this.context, "Loading",
                loadingText, true)
    }

    fun Activity.hideKeyboard() {
        // Check if no view has focus:
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun Activity.showFab(fabId: Int, b: Boolean, func: () -> View.OnClickListener) {
        if (this is HomeActivity && this.fab != null) {
            this.fab!!.setImageDrawable(this.resources.getDrawable(fabId, null))
            this.fab!!.show()
            this.fab!!.setOnClickListener(func())

            this.hideFab = b
        }
    }

    fun Activity.showFab(fabId: Int, func: () -> View.OnClickListener) {
        showFab(fabId, true, func)
    }

    fun Activity.hideFab() {
        if (this is HomeActivity && this.fab != null) {
            this.fab!!.hide()
        }
    }

    fun Activity.hideToolbar() {
        if (this is HomeActivity) {
            this.hideToolbar()
        }
    }

    fun Activity.showToolbar() {
        if (this is HomeActivity) {
            this.showToolbar()
        }
    }
}

