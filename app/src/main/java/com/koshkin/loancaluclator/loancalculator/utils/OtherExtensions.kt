package com.koshkin.loancaluclator.loancalculator.utils

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by tehras on 7/10/16.
 */
fun Fragment.putAttribute(func: Bundle.() -> Unit): Fragment {
    val bundle = Bundle()
    bundle.func()

    this.arguments = bundle

    return this
}