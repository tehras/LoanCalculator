package com.koshkin.loancaluclator.loancalculator.models.loans

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

/**
 * Created by koshkin on 7/4/16.
 */
class Loan {

    @SerializedName("Name")
    lateinit var name: String

    @SerializedName("Provider")
    lateinit var provider: String

    @SerializedName("Balance")
    var balance: Double = 0.0

    @SerializedName("Interest")
    var interest: Double = 0.0

    @SerializedName("Payment")
    var payment: Double = 0.0

    @SerializedName("RepaymentStartDate")
    var repaymentStartDate: Long = 0


    private val PARSER_NAME = "Name"
    private val PARSER_BALANCE = "Balance"
    private val PARSER_INTEREST = "Interest"
    private val PARSER_PAYMENT = "Payment"
    private val PARSER_PROVIDER = "Provider"
    private val PARSER_REPAYMENT_START_DATE = "ReplaymentStartDate"

    fun parse(response: JSONObject): Loan {
        if (response.has(PARSER_NAME)) name = response.getString(PARSER_NAME)
        if (response.has(PARSER_BALANCE)) balance = response.getDouble(PARSER_BALANCE)
        if (response.has(PARSER_INTEREST)) interest = response.getDouble(PARSER_INTEREST)
        if (response.has(PARSER_PAYMENT)) payment = response.getDouble(PARSER_PAYMENT)
        if (response.has(PARSER_PROVIDER)) provider = response.getString(PARSER_PROVIDER)
        if (response.has(PARSER_REPAYMENT_START_DATE)) repaymentStartDate = response.getLong(PARSER_REPAYMENT_START_DATE)

        return this;
    }

    override fun toString(): String {
        return "Loan(repaymentStartDate=$repaymentStartDate, name='$name', balance=$balance, interest=$interest, payment=$payment)"
    }

}


