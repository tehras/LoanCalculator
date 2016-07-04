package com.koshkin.loancaluclator.loancalculator.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.adapters.LandingScreenAdapter
import com.koshkin.loancaluclator.loancalculator.dataholders.loans
import com.koshkin.loancaluclator.loancalculator.dataholders.paymentList
import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList
import com.koshkin.loancaluclator.loancalculator.networking.*
import com.koshkin.loancaluclator.loancalculator.networking.helpers.landingRequest
import com.koshkin.loancaluclator.loancalculator.networking.helpers.paymentsRequest
import com.koshkin.loancaluclator.loancalculator.utils.defaultRecyclerView

/**
 * Created by koshkin on 7/4/16.
 *
 * Landing Screen Fragment
 */
class LandingScreenFragment : BaseFragment(), NetworkResponse {
    override fun onResponse(response: Response, request: Request) {
        Log.d(javaClass.simpleName, "loans response - " + response.parsingObject.toString())
        if (response.parsingObject is Loans)
            if (response.status == Response.ResponseStatus.SUCCESS) {
                adapter.notifyLoansUpdated()
            } else {
                //todo later
            }
        else if (response.parsingObject is PaymentsList) {
            if (response.status == Response.ResponseStatus.SUCCESS) {
                adapter.notifyChartUpdated()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater?.inflate(R.layout.fragment_landing_screen, container, false)

        val recyclerView = v!!.findViewById(R.id.landing_screen_recycler_view) as RecyclerView

        initializeRecyclerView(recyclerView)

        if (loans.status != LoadingStatus.SUCCESS)
            executeLoansCall()
        if (paymentList.status != LoadingStatus.SUCCESS)
            executePaymentsList()

        return v
    }

    private fun executePaymentsList() {
        Runner().paymentsRequest(paymentList, this)
    }

    private fun executeLoansCall() {
        Runner().landingRequest(loans, this)
    }

    lateinit var adapter: LandingScreenAdapter

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        recyclerView.defaultRecyclerView()
        adapter = LandingScreenAdapter(loans, paymentList)
        recyclerView.adapter = adapter
    }

}
