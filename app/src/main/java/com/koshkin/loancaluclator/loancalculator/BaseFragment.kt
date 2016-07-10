package com.koshkin.loancaluclator.loancalculator

import android.app.ProgressDialog
import android.support.v4.app.Fragment
import com.koshkin.loancaluclator.loancalculator.R.anim.abc_grow_fade_in_from_bottom
import com.koshkin.loancaluclator.loancalculator.R.anim.abc_slide_out_top
import android.support.v7.app.AppCompatActivity
import com.koshkin.loancaluclator.loancalculator.fragments.BottomSheetDialogFragment

/**
 * Created by koshkin on 7/4/16.
 *
 * Base Fragment
 */
open class BaseFragment : Fragment() {

    fun addBottomSheet(view: Int, callback: BottomSheetDialogFragment.BottomSheetViewCallback) {
        BottomSheetDialogFragment().addView(view, callback).show(activity.supportFragmentManager, "bottom_sheet_dialog")
    }

    fun startFragment(a: AppCompatActivity, first: Boolean) {
        val tran = a.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
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
        dialog = ProgressDialog.show(this.context, "",
                "Logging in, please wait...", true)
    }
}