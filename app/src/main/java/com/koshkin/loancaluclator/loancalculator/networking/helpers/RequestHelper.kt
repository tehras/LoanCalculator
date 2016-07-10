package com.koshkin.loancaluclator.loancalculator.networking.helpers

import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.NewLoan
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList
import com.koshkin.loancaluclator.loancalculator.networking.*

/**
 * Created by koshkin on 7/4/16.
 *
 * Helper for executing calls
 */
fun Runner.landingRequest(loans: Loans, callbackObj: NetworkResponse) {
    val request = Request(Request.Type.GET, baseUrl + extLoans, loans, callbackObj)

    this.execute(request)
}

fun Runner.paymentsRequest(paymentsList: PaymentsList, callbackObj: NetworkResponse) {
    val request = Request(Request.Type.GET, baseUrl + extPayments, paymentsList, callbackObj)

    this.execute(request)
}

fun Runner.createLoanRequest(loan: NewLoan, callbackObj: NetworkResponse) {
    val request = Request(Request.Type.PUT, baseUrl + extLoans, loan, callbackObj)
    request.requestData = loan.getRequestObject()

    this.execute(request)
}