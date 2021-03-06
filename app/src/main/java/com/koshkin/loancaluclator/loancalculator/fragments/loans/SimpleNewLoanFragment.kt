package com.koshkin.loancaluclator.loancalculator.fragments.loans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.fragments.dialogs.SimpleErrorDialog
import com.koshkin.loancaluclator.loancalculator.models.loans.Loan
import com.koshkin.loancaluclator.loancalculator.networking.NetworkResponse
import com.koshkin.loancaluclator.loancalculator.networking.Request
import com.koshkin.loancaluclator.loancalculator.networking.Response
import com.koshkin.loancaluclator.loancalculator.networking.Runner
import com.koshkin.loancaluclator.loancalculator.networking.helpers.createLoanRequest
import com.koshkin.loancaluclator.loancalculator.utils.defaultAnimate
import com.koshkin.loancaluclator.loancalculator.views.loanform.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tehras on 7/10/16.
 *
 * tehras
 */
class SimpleNewLoanFragment : BaseFragment(), NetworkResponse {
    override fun onResponse(response: Response, request: Request) {
        Log.d(TAG, "response $response")
        if (response.status == Response.ResponseStatus.SUCCESS) {
            hideProgressDialog()
            activity.onBackPressed()
        } else {
            SimpleErrorDialog.create(activity.resources.getString(R.string.error_message_new_loan)).show(activity.supportFragmentManager, "error_dialog")
        }
    }

    companion object Factory {
        fun create(): SimpleNewLoanFragment = SimpleNewLoanFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
        if (loan == null)
            loan = Loan()

        Log.d(TAG, "onCreate arguments - $arguments")
    }

    var loan: Loan? = null

    override fun onPause() {
        activity.showToolbar()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        activity.hideToolbar()
        activity.hideFab()
    }

    var basicInformation: NewLoanBasicInformation? = null
    var balanceInformation: NewLoanBalanceInformation? = null
    var repaymentInformation: NewLoanPaymentInformation? = null
    var finalInformation: NewLoanFinalInformation? = null

    private val STATE_BASIC: Int = 0
    private val STATE_BALANCE: Int = 1
    private val STATE_REPAYMENT: Int = 2
    private val STATE_FINAL: Int = 3

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_simple_new_loan, container, false)

        Log.d(TAG, "onCreateView")

        basicInformation = view.findViewById(R.id.new_loan_basic_information_view) as NewLoanBasicInformation
        balanceInformation = view.findViewById(R.id.new_loan_balance_information_view) as NewLoanBalanceInformation
        repaymentInformation = view.findViewById(R.id.new_loan_payment_information_view) as NewLoanPaymentInformation
        finalInformation = view.findViewById(R.id.new_loan_final_information_view) as NewLoanFinalInformation

        (basicInformation as NewLoanBasicInformation).setValidation { loanName, loanProvider ->
            loan!!.name = loanName.editText.text.toString()
            loan!!.provider = loanProvider.editText.text.toString()

            if (loanName.editText.text.toString().isGreaterThan2() && loanProvider.editText.text.toString().isGreaterThan2()) true
            else false
        }

        (balanceInformation as NewLoanBalanceInformation).setValidation { loanBalance, loanInterest ->
            loan!!.balance = loanBalance.editText.text.toString().toSafeDouble()
            loan!!.interest = loanInterest.editText.text.toString().toSafeDouble()

            if (loanBalance.editText.text.toString().isGreaterThan0() && loanInterest.editText.text.toString().isGreaterThan0()
                    && loanInterest.editText.text.toString().isLessThan100()) true
            else false
        }

        (repaymentInformation as NewLoanPaymentInformation).setValidation { loanBasePayment, loanExtraBasePayment ->
            loan!!.payment = loanBasePayment.editText.text.toString().toSafeDouble()
            loan!!.extraPayment = loanExtraBasePayment.editText.text.toString().toSafeDouble()

            if (loanBasePayment.editText.text.toString().isGreaterThan0() && loanExtraBasePayment.editText.text.toString().isGreaterThan0())
                true
            else false
        }

        (finalInformation as NewLoanFinalInformation).setValidation { calendarView ->
            loan!!.repaymentStartDate = calendarView.selectedDate.toRepaymentStatus()
            true
        }

        addCallbacks(basicInformation!!, balanceInformation!!, repaymentInformation!!, finalInformation!!)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkForSavedState(savedInstanceState)
    }

    val outputDateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)

    fun CalendarDay.toRepaymentStatus(): String {
        return outputDateFormat.format(this.date)
    }

    private fun addCallbacks(basicInformation: NewLoanBasicInformation, balanceInformation: NewLoanBalanceInformation, repaymentInformation: NewLoanPaymentInformation, finalInformation: NewLoanFinalInformation) {
        basicInformation.callback = object : NewLoanCallback {
            override fun next() {
                basicInformation.visibility = View.GONE
                repaymentInformation.visibility = View.GONE
                balanceInformation.visibility = View.VISIBLE
                balanceInformation.animate().defaultAnimate(0) {}
            }
        }

        balanceInformation.callback = object : NewLoanCallback {
            override fun next() {
                repaymentInformation.visibility = View.VISIBLE
                balanceInformation.visibility = View.GONE
                repaymentInformation.animate().defaultAnimate(0) {}
            }

            override fun prev() {
                balanceInformation.visibility = View.GONE
                basicInformation.visibility = View.VISIBLE
                basicInformation.animate().defaultAnimate(0) {}
            }
        }

        repaymentInformation.callback = object : NewLoanCallback {
            override fun next() {
                finalInformation.visibility = View.VISIBLE
                repaymentInformation.visibility = View.GONE
                finalInformation.animate().defaultAnimate(0) {}
            }

            override fun prev() {
                repaymentInformation.visibility = View.GONE
                balanceInformation.visibility = View.VISIBLE
                basicInformation.animate().defaultAnimate(0) {}
            }
        }

        finalInformation.callback = object : NewLoanCallback {
            override fun next() {
                showProgressDialog("Submitting your loan!")
                Runner().createLoanRequest(loan!!, this@SimpleNewLoanFragment)
            }

            override fun prev() {
                finalInformation.visibility = View.GONE
                repaymentInformation.visibility = View.VISIBLE
                basicInformation.animate().defaultAnimate(0) {}
            }
        }
    }

    private val TAG = "SimpleNewLoanFragment"

    /**
     * Validation for Loan Name and Provider
     */
    fun String.isGreaterThan2(): Boolean {
        Log.d(TAG, "length $length - text $this")
        return this.trim().length >= 3
    }

    /**
     * Validation for loan balance
     */
    fun String.isGreaterThan0(): Boolean {
        return this.isNotEmpty() && this.toDouble() > 0
    }

    /**
     * Validation less than 100
     */
    fun String.isLessThan100(): Boolean {
        return this.isNotEmpty() && this.toDouble() < 100
    }

    fun String.toSafeDouble(): Double {
        if (this.isNullOrEmpty())
            return 0.toDouble()

        return this.toDouble()
    }

    private val BUNDLE_KEY_LOAN_NAME = "loan_name_key"
    private val BUNDLE_KEY_LOAN_PROVIDER = "loan_provider"
    private val BUNDLE_KEY_LOAN_BALANCE = "loan_balance"
    private val BUNDLE_KEY_LOAN_INTEREST = "loan_interest"
    private val BUNDLE_KEY_LOAN_BASE_PAYMENT = "loan_base_payment"
    private val BUNDLE_KEY_LOAN_EXTRA_PAYMENT = "loan_extra_payment"
    private val BUNDLE_KEY_LOAN_CARD_SHOWING = "loan_card_showing"

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        Log.d(TAG, "onSaveInstanceState")
        //save all the fields
        outState!!.putString(BUNDLE_KEY_LOAN_NAME, basicInformation!!.loanName.editText.text.toString())
        outState.putString(BUNDLE_KEY_LOAN_PROVIDER, basicInformation!!.loanProvider.editText.text.toString())
        outState.putString(BUNDLE_KEY_LOAN_BALANCE, balanceInformation!!.loanBalance.editText.text.toString())
        outState.putString(BUNDLE_KEY_LOAN_INTEREST, balanceInformation!!.loanInterest.editText.text.toString())
        outState.putString(BUNDLE_KEY_LOAN_BASE_PAYMENT, repaymentInformation!!.loanBasePayment.editText.text.toString())
        outState.putString(BUNDLE_KEY_LOAN_EXTRA_PAYMENT, repaymentInformation!!.loanExtraPayment.editText.text.toString())
        outState.putInt(BUNDLE_KEY_LOAN_CARD_SHOWING, getCurrentCard())
    }

    private fun getCurrentCard(): Int {
        if (basicInformation!!.visibility == View.VISIBLE) return STATE_BASIC
        else if (balanceInformation!!.visibility == View.VISIBLE) return STATE_BALANCE
        else if (repaymentInformation!!.visibility == View.VISIBLE) return STATE_REPAYMENT
        else return STATE_FINAL
    }

    private fun showCorrectCard(cardState: Int) {
        basicInformation!!.visibility = View.GONE
        balanceInformation!!.visibility = View.GONE
        repaymentInformation!!.visibility = View.GONE
        finalInformation!!.visibility = View.GONE

        when (cardState) {
            STATE_BASIC -> basicInformation!!.visibility = View.VISIBLE
            STATE_BALANCE -> balanceInformation!!.visibility = View.VISIBLE
            STATE_REPAYMENT -> repaymentInformation!!.visibility = View.VISIBLE
            else -> finalInformation!!.visibility = View.VISIBLE
        }
    }

    private fun checkForSavedState(savedInstanceState: Bundle?) {
        Log.d(TAG, "checkForSavedState - $savedInstanceState")
        if (savedInstanceState != null) {
            savedInstanceState.getString(BUNDLE_KEY_LOAN_NAME, "") { basicInformation!!.loanName.editText.setText(it) }
            savedInstanceState.getString(BUNDLE_KEY_LOAN_PROVIDER, "") { basicInformation!!.loanProvider.editText.setText(it) }
            savedInstanceState.getString(BUNDLE_KEY_LOAN_BALANCE, "") { balanceInformation!!.loanBalance.editText.setText(it) }
            savedInstanceState.getString(BUNDLE_KEY_LOAN_INTEREST, "") { balanceInformation!!.loanInterest.editText.setText(it) }
            savedInstanceState.getString(BUNDLE_KEY_LOAN_BASE_PAYMENT, "") { repaymentInformation!!.loanBasePayment.editText.setText(it) }
            savedInstanceState.getString(BUNDLE_KEY_LOAN_EXTRA_PAYMENT, "") { repaymentInformation!!.loanExtraPayment.editText.setText(it) }

            showCorrectCard(savedInstanceState.getInt(BUNDLE_KEY_LOAN_CARD_SHOWING, STATE_BASIC))
        }
    }

    fun Bundle.getString(key: String, default: String, func: Loan.(s: String) -> Unit) {
        val s = this.getString(key, default)
        if (s.isNotEmpty())
            loan!!.func(s)
    }
}
