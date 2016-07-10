package com.koshkin.loancaluclator.loancalculator.models.payments

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by koshkin on 7/4/16.
 */
class Payment {

    @SerializedName("Balance")
    var balance: Double = 0.0
    @SerializedName("Payment")
    var payment: Double = 0.0
    @SerializedName("Interest")
    var interest: Double = 0.0
    @SerializedName("Principal")
    var principal: Double = 0.0
    @SerializedName("PaymentDate")
    var paymentDate: String = ""

    val dateFormatter = SimpleDateFormat("yyyyMM", Locale.US)

    fun getFormattedDate(): Long {
        return dateFormatter.parse(paymentDate).time
    }

    override fun toString(): String {
        return "Payment(balance=$balance, payment=$payment, interest=$interest, principal=$principal, paymentDate='$paymentDate', dateFormatter=$dateFormatter)"
    }
}