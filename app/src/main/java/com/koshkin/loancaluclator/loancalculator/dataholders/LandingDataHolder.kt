package com.koshkin.loancaluclator.loancalculator.dataholders

import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList

/**
 * Created by koshkin on 7/4/16.
 *
 * Saving static objects
 */

var loans: Loans = Loans()
var paymentList: PaymentsList = PaymentsList()

fun reset() {
    loans = Loans()
    paymentList = PaymentsList()
}

