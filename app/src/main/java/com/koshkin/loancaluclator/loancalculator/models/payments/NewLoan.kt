package com.koshkin.loancaluclator.loancalculator.models.payments

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.koshkin.loancaluclator.loancalculator.networking.ParsingObject

/**
 * Created by tehras on 7/9/16.
 */
class NewLoan : ParsingObject {
    @SerializedName("Name")
    var name: String? = null

    @SerializedName("Provider")
    var provider: String? = null

    @SerializedName("Balance")
    var balance: Double? = null

    @SerializedName("Interest")
    var interest: Double? = null

    @SerializedName("BasePayment")
    var basePayment: Double? = null

    @SerializedName("ExtraPayment")
    var extraPayment: Double? = null

    @SerializedName("RepaymentStartDate")
    var repaymentStartDate: String? = null

    fun getRequestObject(): String {
        return Gson().toJson(this)
    }

    override fun parse(response: String) {

    }

}