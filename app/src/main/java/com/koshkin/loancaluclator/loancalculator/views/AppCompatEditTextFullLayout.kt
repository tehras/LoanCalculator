package com.koshkin.loancaluclator.loancalculator.views

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.InputType
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
        isSaveEnabled = true
        val inputType: Int
        val lines: Int

        val a = context!!.theme.obtainStyledAttributes(attributes, R.styleable.AppCompatEditTextFullLayout, 0, 0)

        try {
            inputType = a.getInt(R.styleable.AppCompatEditTextFullLayout_android_inputType, InputType.TYPE_CLASS_TEXT)
            lines = a.getInt(R.styleable.AppCompatEditTextFullLayout_lines, 1)
        } finally {
            a.recycle()
        }

        inflate(context, R.layout.view_app_compat_edit_text_full_layout, this)

        editText = findViewById(R.id.view_app_compat_edit_text) as TextInputEditText

        editText.setHintTextColor(Color.WHITE)
        editText.inputType = inputType
        editText.maxLines = lines
    }
}
