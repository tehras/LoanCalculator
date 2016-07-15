package com.koshkin.loancaluclator.loancalculator.models.loans

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.koshkin.loancaluclator.loancalculator.networking.ParsingObject
import org.json.JSONObject
import java.io.Serializable

/**
 * Created by koshkin on 7/4/16.
 *
 * Loan
 */
class Loan : Serializable, ParsingObject {
    override fun parse(response: String) {

    }

    fun getRequestObject(): String {
        return Gson().toJson(this)
    }

    constructor(key: String) {
        this@Loan.key = key
    }

    constructor()

    lateinit var key: String

    @SerializedName("Name")
    var name: String = ""

    @SerializedName("Provider")
    var provider: String = ""

    @SerializedName("Balance")
    var balance: Double = 0.0

    @SerializedName("Interest")
    var interest: Double = 0.0

    @SerializedName("BasePayment")
    var payment: Double = 0.0

    @SerializedName("ExtraPayment")
    var extraPayment: Double = 0.0

    @SerializedName("RepaymentStartDate")
    var repaymentStartDate: String = ""

    private val PARSER_NAME = "Name"
    private val PARSER_BALANCE = "Balance"
    private val PARSER_INTEREST = "Interest"
    private val PARSER_PAYMENT = "BasePayment"
    private val PARSER_EXTRA_PAYMENT = "ExtraPayment"
    private val PARSER_PROVIDER = "Provider"
    private val PARSER_REPAYMENT_START_DATE = "RepaymentStartDate"

    fun parse(response: JSONObject): Loan {
        if (response.has(PARSER_NAME)) name = response.getString(PARSER_NAME)
        if (response.has(PARSER_BALANCE)) balance = response.getDouble(PARSER_BALANCE)
        if (response.has(PARSER_INTEREST)) interest = response.getDouble(PARSER_INTEREST)
        if (response.has(PARSER_PAYMENT)) payment = response.getDouble(PARSER_PAYMENT)
        if (response.has(PARSER_PAYMENT)) extraPayment = response.getDouble(PARSER_EXTRA_PAYMENT)
        if (response.has(PARSER_PROVIDER)) provider = response.getString(PARSER_PROVIDER)
        if (response.has(PARSER_REPAYMENT_START_DATE)) repaymentStartDate = response.getString(PARSER_REPAYMENT_START_DATE)

        return this
    }

    override fun toString(): String {
        return "Loan(repaymentStartDate=$repaymentStartDate, name='$name', balance=$balance, interest=$interest, payment=$payment)"
    }

}


