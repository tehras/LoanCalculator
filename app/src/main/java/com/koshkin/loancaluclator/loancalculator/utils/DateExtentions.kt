package com.koshkin.loancaluclator.loancalculator.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat

fun String.asOfDate(inputFormat: SimpleDateFormat, outputDate: SimpleDateFormat): String {
    var date = "N/A"
    try {
        date = outputDate.format(inputFormat.parse(this.toString()))
    } catch (e: ParseException) {
        Log.d(javaClass.simpleName, "Exception - ", e)
    }
    return date
}
