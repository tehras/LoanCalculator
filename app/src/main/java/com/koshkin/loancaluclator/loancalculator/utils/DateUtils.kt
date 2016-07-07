package com.koshkin.loancaluclator.loancalculator.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by koshkin on 7/4/16.
 *
 * Date Utils
 */
fun Long.asOfDate(inputFormat: SimpleDateFormat, outputDate: SimpleDateFormat): String {
    var date = "N/A"
    try {
        date = outputDate.format(inputFormat.parse(this.toString()))
    } catch (e: ParseException) {
        Log.d(javaClass.simpleName, "Exception - ", e)
    }
    return date
}
