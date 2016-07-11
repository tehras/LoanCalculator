package com.koshkin.loancaluclator.loancalculator.views

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import com.koshkin.loancaluclator.loancalculator.R

/**
 * Created by tehras on 7/10/16.
 *
 * App Compat Edit Text
 */
class AppCompatEditTextFullLayout(context: Context?, attributes: AttributeSet?, defStyleAttr: Int) : TextInputLayout(context, attributes, defStyleAttr) {
    constructor(context: Context?, attributes: AttributeSet) : this(context, attributes, 0)

    constructor(context: Context?) : this(context, null, 0)

    lateinit var editText: TextInputEditText

    init {
        inflate(context, R.layout.view_app_compat_edit_text_full_layout, this)

        editText = findViewById(R.id.view_app_compat_edit_text) as TextInputEditText
    }
}
