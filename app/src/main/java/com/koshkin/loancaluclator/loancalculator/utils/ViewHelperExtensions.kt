package com.koshkin.loancaluclator.loancalculator.utils

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import com.koshkin.loancaluclator.loancalculator.views.loanform.NewLoanAnimationListener

/**
 * Created by tehras on 7/11/16.
 *
 * Extensions for views
 */
fun ViewPropertyAnimator.defaultAnimate(translationX: Int, func: ViewPropertyAnimator.() -> Unit) {
    this.setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .setListener(object : NewLoanAnimationListener {
                override fun onAnimationEnd(p0: Animator?) {
                    func()
                }
            }).translationX(translationX.toFloat())
            .start()
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}
