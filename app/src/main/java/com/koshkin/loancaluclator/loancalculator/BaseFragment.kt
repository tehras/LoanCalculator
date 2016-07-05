package com.koshkin.loancaluclator.loancalculator

import android.support.v4.app.Fragment
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

}