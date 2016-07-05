package com.koshkin.loancaluclator.loancalculator.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.AxisValueFormatter
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.models.payments.Payment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by koshkin on 7/4/16.
 *
 * Landing Chart View
 */
class LandingChartView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    var lineChart: LineChart

    init {
        View.inflate(context, R.layout.view_landing_chart_layout, this)

        lineChart = findViewById(R.id.landing_chart_view) as LineChart
        lineChart.initChartView()
    }

    fun LineChart.initChartView() {
        this.axisRight.isEnabled = false
        this.axisLeft.isEnabled = false

        this.xAxis.isEnabled = true
        this.xAxis.setDrawGridLines(false)
        this.xAxis.setDrawLabels(true)
        this.xAxis.setDrawAxisLine(true)
        this.xAxis.axisLineColor = Color.WHITE
        this.xAxis.textColor = Color.WHITE
        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
        this.xAxis.setAvoidFirstLastClipping(false)
        this.xAxis.valueFormatter = xAxisValueFormatter

        this.setDescription("")
        this.setNoDataText("Loading, Please Wait")
        this.setDescriptionColor(Color.WHITE)
        this.setNoDataTextColor(Color.WHITE)

        this.setTouchEnabled(false)
        this.extraBottomOffset = 20.toFloat()
        this.extraTopOffset = 10.toFloat()
    }

    fun updateData(payments: ArrayList<Payment>) {
        val data: LineData = LineData()

        val vals: ArrayList<Entry> = ArrayList()

        payments.forEach {
            vals.add(Entry(it.getFormattedDate().toFloat(), it.balance.toFloat()))
        }

        val dataSet: LineDataSet = LineDataSet(vals, "")
        dataSet.initBalanceProperties()

        data.addDataSet(dataSet)
        data.initBalanceProperties()
        lineChart.data = data
        lineChart.animateX(500)
    }

    fun updateDescriptionError() {
        lineChart.setNoDataText("Error retrieving data")
    }

    fun LineDataSet.initBalanceProperties() {
        this.setDrawCircles(false)

        this.lineWidth = 2.0f
        this.color = lineChart.context.resources.getColor(R.color.landing_line_color, null)
        this.label = ""

        this.mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    fun LineData.initBalanceProperties() {
        this.setDrawValues(false)
    }

    object xAxisValueFormatter : AxisValueFormatter {
        val displayFormatter = SimpleDateFormat("MMM yy", Locale.US)

        override fun getDecimalDigits(): Int {
            return -1
        }

        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return displayFormatter.format(Date(value.toLong()))
        }
    }

}
