package com.koshkin.loancaluclator.loancalculator.fragments.loans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.views.loanform.NewLoanBasicInformation

/**
 * Created by tehras on 7/10/16.
 *
 * tehras
 */
class SimpleNewLoanFragment : BaseFragment() {
    companion object Factory {
        fun create(): SimpleNewLoanFragment = SimpleNewLoanFragment()
    }

    override fun onResume() {
        super.onResume()

        activity.hideFab()
    }

    var basicInformation: NewLoanBasicInformation? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_simple_new_loan, container, false)

        basicInformation = view.findViewById(R.id.new_loan_basic_information_view) as NewLoanBasicInformation
        (basicInformation as NewLoanBasicInformation).setValidation() { loanName, loanProvider ->
            if (loanName.editText.text.toString().isGreaterThan2() && loanProvider.editText.text.toString().isGreaterThan2()) true
            else false
        }

        return view
    }

    private val TAG = "SimpleNewLoanFragment"

    fun String.isGreaterThan2(): Boolean {
        Log.d(TAG, "length $length - text $this")
        return this.trim().length >= 3
    }
}
