package com.koshkin.loancaluclator.loancalculator.fragments

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.dataholders.loans
import com.koshkin.loancaluclator.loancalculator.models.payments.NewLoan
import com.koshkin.loancaluclator.loancalculator.networking.*
import com.koshkin.loancaluclator.loancalculator.networking.helpers.createLoanRequest

/**
 * Created by tehras on 7/9/16.
 *
 * Base Fragment
 */
class NewLoanFragment : BaseFragment(), NetworkResponse {

    override fun onResume() {
        super.onResume()

        activity.showFab(R.drawable.ic_accept_drawable_white) { View.OnClickListener { validateAndSendLoan() } }
    }

    companion object Factory {
        fun create(): NewLoanFragment = NewLoanFragment()
    }

    var loanName: TextInputEditText? = null
    var loanProvider: TextInputEditText? = null
    var loanAmount: TextInputEditText? = null
    var loanInterest: TextInputEditText? = null
    var loanBaseRepayment: TextInputEditText? = null
    var loanExtraRepayment: TextInputEditText? = null
    var loanRepaymentStartDate: TextInputEditText? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_new_loan, container, false)

        view.setOnTouchListener { p0, p1 ->
            activity.hideKeyboard()
            false
        }

        loanName = view.findViewById(R.id.new_loan_name_field) as TextInputEditText
        loanProvider = view.findViewById(R.id.new_loan_provider_field) as TextInputEditText
        loanAmount = view.findViewById(R.id.new_loan_amount_field) as TextInputEditText
        loanBaseRepayment = view.findViewById(R.id.new_loan_base_repayment_field) as TextInputEditText
        loanInterest = view.findViewById(R.id.new_loan_interest_field) as TextInputEditText
        loanExtraRepayment = view.findViewById(R.id.new_loan_extra_repayment_field) as TextInputEditText
        loanRepaymentStartDate = view.findViewById(R.id.new_loan_repayment_start_date_field) as TextInputEditText

//        (view.findViewById(R.id.new_loan_accept_button) as FloatingActionButton).setOnClickListener { validateAndSendLoan() }

        return view
    }

    var newLoan: NewLoan = NewLoan()

    private fun validateAndSendLoan() {
        //todo validate

        //"yyyyMMdd"
        newLoan.name = loanName!!.text.toString()
        newLoan.provider = loanProvider!!.text.toString()
        newLoan.balance = loanAmount!!.text.toString().toDouble()
        newLoan.basePayment = loanBaseRepayment!!.text.toString().toDouble()
        newLoan.extraPayment = loanExtraRepayment!!.text.toString().toDouble()
        newLoan.repaymentStartDate = loanRepaymentStartDate!!.text.toString()
        newLoan.interest = loanInterest!!.text.toString().toDouble()

        Log.d(TAG, "validate")
        createNewLoan(newLoan)
    }

    val TAG = "NewLoanFragment"

    private fun createNewLoan(loan: NewLoan) {
        Runner().createLoanRequest(loan, this)
    }


    override fun onResponse(response: Response, request: Request) {
        if (response.status == Response.ResponseStatus.SUCCESS) {
            loans.status = LoadingStatus.LOADING
            activity.onBackPressed()
        } else {
            //todo error popup
        }
    }

}