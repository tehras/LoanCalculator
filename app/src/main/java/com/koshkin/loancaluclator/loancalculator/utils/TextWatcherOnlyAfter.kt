package com.koshkin.loancaluclator.loancalculator.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by tehras on 7/10/16.
 */
open class TextWatcherOnlyAfter : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        //leave empty
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //leave empty
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //leave mepty
    }

}