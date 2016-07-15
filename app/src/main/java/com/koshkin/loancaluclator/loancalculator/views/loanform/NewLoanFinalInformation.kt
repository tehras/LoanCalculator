package com.koshkin.loancaluclator.loancalculator.views.loanform

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.utils.defaultAnimate
import com.koshkin.loancaluclator.loancalculator.utils.hideKeyboard
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*

/**
 * Created by tehras on 7/11/16.
 *
 * New Loan Balance Info
 */
class NewLoanFinalInformation(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    @Suppress("unused")
    private val TAG = "NewLoanBalanceInformation"

    lateinit var calendarLayout: MaterialCalendarView
    lateinit var submitImage: FloatingActionButton
    lateinit var prevImage: FloatingActionButton

    var callback: NewLoanCallback? = null

    init {
        inflate(context, R.layout.view_loan_final_information, this)

        calendarLayout = findViewById(R.id.new_loan_calendar_layout) as MaterialCalendarView
        calendarLayout.newState()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMaximumDate(getMaxCalendarDate())
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        submitImage = findViewById(R.id.view_loan_final_next) as FloatingActionButton
        submitImage.visibility = View.INVISIBLE
        submitImage.setOnClickListener { goToNext() }

        prevImage = findViewById(R.id.view_loan_final_previous) as FloatingActionButton
        prevImage.visibility = View.VISIBLE
        prevImage.setOnClickListener { goToPrev() }

        this.setOnClickListener { hideKeyboard() }
    }

    private fun getMaxCalendarDate(): CalendarDay {
        val c = Calendar.getInstance()

        return CalendarDay.from(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
    }

    @Suppress("unused")
    fun setValidation(func: (calendarView: MaterialCalendarView) -> Boolean) {
        calendarLayout.setOnDateChangedListener { materialCalendarView, calendarDay, b ->
            if (func(materialCalendarView)) showNext()
            else hideNext()
        }
    }

    private fun goToNext() {
        animate().defaultAnimate(-measuredWidth) { if (callback != null) callback!!.next() }
    }

    private fun goToPrev() {
        animate().defaultAnimate(measuredWidth) { if (callback != null) callback!!.prev() }
    }

    private var enableNext: Boolean = false

    private fun showNext() {
        submitImage.show()
        enableNext = true
    }

    private fun hideNext() {
        submitImage.hide()
        enableNext = false
    }

}
