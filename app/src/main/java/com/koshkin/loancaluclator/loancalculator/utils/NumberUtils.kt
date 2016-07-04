package com.koshkin.loancaluclator.loancalculator.utils

import java.text.DecimalFormat

/**
 * Created by koshkin on 7/4/16.
 */

var amountDisplayFormatter = DecimalFormat("#,###,###,##0.00")

fun Double.numberDisplay(): String {
    return "$" + amountDisplayFormatter.format(this)
}
