package com.koshkin.loancaluclator.loancalculator.models.loans

import com.koshkin.loancaluclator.loancalculator.networking.LoadingStatus
import com.koshkin.loancaluclator.loancalculator.networking.ParsingObject
import org.json.JSONObject
import java.util.*

/**
 * Created by koshkin on 7/4/16.
 */
class Loans : ParsingObject {

    var loans: HashMap<String, Loan> = HashMap()
    var status: LoadingStatus = LoadingStatus.LOADING

    override fun parse(response: String) {
        val reader = JSONObject(response)

        val iterator = reader.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val o = reader.get(key)
            if (o is JSONObject) {
                o.parse(key)
            }
        }
    }

    fun JSONObject.parse(key: String) {
        status = LoadingStatus.SUCCESS

        loans.put(key, Loan().parse(this))
    }

    override fun toString(): String {
        return "Loans(loans=$loans, status=$status)"
    }
}