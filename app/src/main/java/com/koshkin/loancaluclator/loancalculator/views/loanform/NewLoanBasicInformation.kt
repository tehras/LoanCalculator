package com.koshkin.loancaluclator.loancalculator.views.loanform

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.utils.TextWatcherOnlyAfter
import com.koshkin.loancaluclator.loancalculator.views.AppCompatEditTextFullLayout

/**
 * Created by tehras on 7/10/16.
 *
 * New Loan Basic Info
 */
class NewLoanBasicInformation(context: Context, attributeSet: AttributeSet?, style: Int) : LinearLayout(context, attributeSet, style) {
    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    @Suppress("unused")
    private val TAG = "NewLoanBasicInfo"

    lateinit var loanName: AppCompatEditTextFullLayout
    lateinit var loanProvider: AppCompatEditTextFullLayout
    lateinit var nextImage: FloatingActionButton

    init {
        inflate(context, R.layout.view_loan_basic_information, this)

        loanName = findViewById(R.id.view_loan_information_name) as AppCompatEditTextFullLayout
        loanProvider = findViewById(R.id.view_loan_information_provider_name) as AppCompatEditTextFullLayout

        nextImage = findViewById(R.id.view_loan_next) as FloatingActionButton
        nextImage.visibility = View.GONE
        nextImage.setOnClickListener { goToNext() }
    }

    private var enableNext: Boolean = false

    fun goToNext() {
        animate().setDuration(200).translationX(-measuredWidth.toFloat()).setInterpolator(AccelerateInterpolator()).start()

        Toast.makeText(context, "Next", Toast.LENGTH_LONG).show()
    }

    @Suppress("unused")
    fun setValidation(func: (loanName: AppCompatEditTextFullLayout, loanProvider: AppCompatEditTextFullLayout) -> Boolean) {
        loanName.editText.watchText(func)
        loanProvider.editText.watchText(func)
    }

    private fun TextInputEditText.watchText(func: (AppCompatEditTextFullLayout, AppCompatEditTextFullLayout) -> Boolean) {
        this.addTextChangedListener(object : TextWatcherOnlyAfter() {
            override fun afterTextChanged(p0: Editable?) {
                if (func(loanName, loanProvider)) showNext()
                else hideNext()
            }
        })
    }

    private fun showNext() {
        nextImage.show()
        enableNext = true
    }

    private fun hideNext() {
        nextImage.hide()
        enableNext = false
    }

}
