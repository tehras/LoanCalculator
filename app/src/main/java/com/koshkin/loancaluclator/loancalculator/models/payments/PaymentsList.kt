package com.koshkin.loancaluclator.loancalculator.models.payments

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.koshkin.loancaluclator.loancalculator.networking.LoadingStatus
import com.koshkin.loancaluclator.loancalculator.networking.ParsingObject
import java.util.*

/**
 * Created by koshkin on 7/4/16.
 */

class PaymentsList : ParsingObject {

    @SerializedName("AggregatedPaymentList")
    var aggregatedPaymentList: ArrayList<Payment> = ArrayList()

    var status: LoadingStatus = LoadingStatus.LOADING

    override fun parse(response: String) {
        status = LoadingStatus.SUCCESS

        aggregatedPaymentList = Gson().fromJson(response, this.javaClass).aggregatedPaymentList
    }

    override fun toString(): String {
        return "PaymentsList(aggregatedPaymentList=$aggregatedPaymentList, status=$status)"
    }
}
