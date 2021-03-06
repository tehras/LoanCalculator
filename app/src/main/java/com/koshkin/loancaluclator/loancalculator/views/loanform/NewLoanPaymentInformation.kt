package com.koshkin.loancaluclator.loancalculator.views.loanform

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.utils.TextWatcherOnlyAfter
import com.koshkin.loancaluclator.loancalculator.utils.defaultAnimate
import com.koshkin.loancaluclator.loancalculator.utils.hideKeyboard
import com.koshkin.loancaluclator.loancalculator.views.AppCompatEditTextFullLayout

/**
 * Created by tehras on 7/11/16.
 *
 * New Loan Balance Info
 */
class NewLoanPaymentInformation(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    @Suppress("unused")
    private val TAG = "NewLoanBalanceInformation"

    lateinit var loanBasePayment: AppCompatEditTextFullLayout
    lateinit var loanExtraPayment: AppCompatEditTextFullLayout
    lateinit var nextImage: FloatingActionButton
    lateinit var prevImage: FloatingActionButton

    var callback: NewLoanCallback? = null

    init {
        inflate(context, R.layout.view_loan_payment_information, this)

        loanBasePayment = findViewById(R.id.view_loan_base_repayment) as AppCompatEditTextFullLayout
        loanExtraPayment = findViewById(R.id.view_loan_extra_repayment) as AppCompatEditTextFullLayout

        nextImage = findViewById(R.id.view_loan_payments_next) as FloatingActionButton
        nextImage.visibility = View.INVISIBLE
        nextImage.setOnClickListener { goToNext() }

        prevImage = findViewById(R.id.view_loan_payments_back) as FloatingActionButton
        prevImage.setOnClickListener { goToPrev() }

        loanBasePayment.editText.isFocusable = true
        loanExtraPayment.editText.isFocusable = true

        this.setOnClickListener { hideKeyboard() }
    }

    @Suppress("unused")
    fun setValidation(func: (loanName: AppCompatEditTextFullLayout, loanProvider: AppCompatEditTextFullLayout) -> Boolean) {
        loanBasePayment.editText.watchText(func)
        loanExtraPayment.editText.watchText(func)
    }

    fun TextInputEditText.watchText(func: (AppCompatEditTextFullLayout, AppCompatEditTextFullLayout) -> Boolean) {
        this.addTextChangedListener(object : TextWatcherOnlyAfter() {
            override fun afterTextChanged(p0: Editable?) {
                if (func(loanBasePayment, loanExtraPayment)) showNext()
                else hideNext()
            }
        })
    }

    private fun goToNext() {
        animate().defaultAnimate(-measuredWidth) { if (callback != null) callback!!.next() }
    }

    private fun goToPrev() {
        animate().defaultAnimate(measuredWidth) { if (callback != null) callback!!.prev() }
    }

    private var enableNext: Boolean = false

    private fun showNext() {
        nextImage.show()
        enableNext = true
    }

    private fun hideNext() {
        nextImage.hide()
        enableNext = false
    }

}
