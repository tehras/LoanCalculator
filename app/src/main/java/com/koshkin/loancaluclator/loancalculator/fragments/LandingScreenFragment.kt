package com.koshkin.loancaluclator.loancalculator.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.adapters.LandingScreenAdapter
import com.koshkin.loancaluclator.loancalculator.dataholders.loans
import com.koshkin.loancaluclator.loancalculator.dataholders.paymentList
import com.koshkin.loancaluclator.loancalculator.models.loans.Loan
import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList
import com.koshkin.loancaluclator.loancalculator.networking.*
import com.koshkin.loancaluclator.loancalculator.networking.helpers.landingRequest
import com.koshkin.loancaluclator.loancalculator.networking.helpers.paymentsRequest
import com.koshkin.loancaluclator.loancalculator.utils.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by koshkin on 7/4/16.
 *
 * Landing Screen Fragment
 */
class LandingScreenFragment : BaseFragment(), NetworkResponse {
    companion object Factory {
        /**
         * Return new instance of the fragment
         */
        fun create(): LandingScreenFragment = LandingScreenFragment()
    }

    override fun onResponse(response: Response, request: Request) {
        Log.d(javaClass.simpleName, "loans response - " + response.parsingObject.toString())
        if (response.parsingObject is Loans) {
            if (response.status == Response.ResponseStatus.FAILURE)
                (response.parsingObject as Loans).status = LoadingStatus.FAILURE
            adapter.notifyLoansUpdated()
            callsToExecute--
        } else if (response.parsingObject is PaymentsList) {
            if (response.status == Response.ResponseStatus.FAILURE)
                (response.parsingObject as PaymentsList).status = LoadingStatus.FAILURE
            adapter.notifyChartUpdated()
            callsToExecute--
        }

        if (callsToExecute <= 0)
            hideProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.fragment_landing_screen, container, false)

        val recyclerView = v!!.findViewById(R.id.landing_screen_recycler_view) as RecyclerView

        initializeRecyclerView(recyclerView)

        if (loans.status != LoadingStatus.SUCCESS)
            executeLoansCall()
        if (paymentList.status != LoadingStatus.SUCCESS)
            executePaymentsList()

        if (callsToExecute > 0)
            showProgressDialog(loadingText)

        return v
    }

    val loadingText = "Loading data, please wait"

    var callsToExecute = 0

    private fun executePaymentsList() {
        callsToExecute++
        Runner().paymentsRequest(paymentList, this)
    }

    private fun executeLoansCall() {
        callsToExecute++
        Runner().landingRequest(loans, this)
    }

    lateinit var adapter: LandingScreenAdapter

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        recyclerView.defaultRecyclerView()
        recyclerView.setExtraBottomPadding(context.resources.getDimensionPixelOffset(R.dimen.activity_vertical_margin))

        adapter = object : LandingScreenAdapter(loans, paymentList) {
            override fun onLoanClicked(loan: Loan) {
                addBottomSheet(R.layout.layout_bottom_sheet_loan, object : BottomSheetDialogFragment.BottomSheetViewCallback {
                    var inputDateFormat: SimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
                    var outputDateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

                    override fun manageView(view: View) {
                        (view.findViewById(R.id.landing_bottom_sheet_interest) as TextView).text = loan.interest.interestDisplay()
                        (view.findViewById(R.id.landing_bottom_sheet_balance) as TextView).text = loan.balance.numberDisplay()
                        (view.findViewById(R.id.landing_bottom_sheet_name) as TextView).text = loan.name
                        (view.findViewById(R.id.landing_bottom_sheet_payments) as TextView).text = loan.payment.toString().payments()
                        (view.findViewById(R.id.landing_bottom_sheet_as_of_date) as TextView).text = loan.repaymentStartDate.asOfDate(inputDateFormat, outputDateFormat)

                    }
                })
            }

        }
        recyclerView.adapter = adapter
    }

}
