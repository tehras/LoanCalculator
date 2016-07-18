package com.koshkin.loancaluclator.loancalculator.fragments.landing

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
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
import com.koshkin.loancaluclator.loancalculator.fragments.bottom_sheets.BottomSheetDialogFragment
import com.koshkin.loancaluclator.loancalculator.fragments.dialogs.SimpleActionableDialog
import com.koshkin.loancaluclator.loancalculator.fragments.dialogs.SimpleErrorDialog
import com.koshkin.loancaluclator.loancalculator.fragments.loans.EditLoanFragment
import com.koshkin.loancaluclator.loancalculator.fragments.loans.SimpleNewLoanFragment
import com.koshkin.loancaluclator.loancalculator.models.loans.Loan
import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList
import com.koshkin.loancaluclator.loancalculator.networking.*
import com.koshkin.loancaluclator.loancalculator.networking.helpers.deleteLoan
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

    override fun onResume() {
        super.onResume()

        activity.showFab(R.drawable.ic_add_white) { View.OnClickListener { SimpleNewLoanFragment.create().startFragment(activity as AppCompatActivity, false) } }
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
        } else {
            Log.d(javaClass.simpleName, "Deleting status ${response.status}")
            //delete
            if (response.status == Response.ResponseStatus.SUCCESS) {
                refreshData()
            } else {
                SimpleErrorDialog.create(activity.resources.getString(R.string.error_message_deleting_loan))
                        .show(activity.supportFragmentManager, "delete_popup")
            }
        }

        if (callsToExecute <= 0)
            hideProgressDialog()
    }

    private fun refreshData() {
        executeLoansCall()
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

                    override fun manageView(view: View, bottomSheet: BottomSheetDialogFragment) {
                        (view.findViewById(R.id.landing_bottom_sheet_interest) as TextView).text = loan.interest.interestDisplay()
                        (view.findViewById(R.id.landing_bottom_sheet_balance) as TextView).text = loan.balance.numberDisplay()
                        (view.findViewById(R.id.landing_bottom_sheet_name) as TextView).text = loan.name
                        (view.findViewById(R.id.landing_bottom_sheet_payments) as TextView).text = loan.payment.numberDisplay()
                        (view.findViewById(R.id.landing_bottom_sheet_as_of_date) as TextView).text = loan.repaymentStartDate.asOfDate(inputDateFormat, outputDateFormat)
                        view.findViewById(R.id.landing_bottom_sheet_edit_button).setOnClickListener { startEditFragment(loan.key) }
                        view.findViewById(R.id.landing_bottom_sheet_delete).setOnClickListener {
                            bottomSheet.dismiss()
                            startDeleteConfirmDialog(loan)
                        }
                    }
                })
            }

        }
        recyclerView.adapter = adapter
    }

    private fun startDeleteConfirmDialog(loan: Loan) {
        SimpleActionableDialog.Builder()
                .setMessage(Html.fromHtml("Are you sure you want to delete <b>${loan.name}</b> loan"))
                .displayCancel(true)
                .callback(object : SimpleActionableDialog.SimpleActionableCallback {
                    override fun submitAction() {
                        Runner().deleteLoan(loan, this@LandingScreenFragment)
                        showProgressDialog("Deleting, please wait.")
                    }
                }).create().show(activity.supportFragmentManager, "delete_confirm_dialog")
    }

    private fun startEditFragment(loanKey: String) {
        EditLoanFragment.create(loanKey).startFragment(activity as AppCompatActivity)
    }

}
