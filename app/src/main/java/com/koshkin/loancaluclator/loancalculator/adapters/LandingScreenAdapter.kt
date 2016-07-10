package com.koshkin.loancaluclator.loancalculator.adapters

import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koshkin.loancaluclator.loancalculator.R
import com.koshkin.loancaluclator.loancalculator.models.loans.Loan
import com.koshkin.loancaluclator.loancalculator.models.loans.Loans
import com.koshkin.loancaluclator.loancalculator.models.payments.PaymentsList
import com.koshkin.loancaluclator.loancalculator.networking.LoadingStatus
import com.koshkin.loancaluclator.loancalculator.utils.numberDisplay
import com.koshkin.loancaluclator.loancalculator.views.LandingChartView

/**
 * Created by koshkin on 7/4/16.
 *
 * Landing Screen Adapter
 */
abstract class LandingScreenAdapter(var loans: Loans, var paymentsList: PaymentsList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is LoanHolder) {
            val loan = loans.loans[loans.loans.keys.elementAt(position - 1)] as Loan

            holder.loanProvider.text = loan.provider
            holder.loanName.text = loan.name
            holder.loanAmount.text = loan.balance.numberDisplay()

            holder.itemView.setOnClickListener({ onLoanClicked(loan) })
        } else if (holder is ChartHolder) {
            if (paymentsList.status == LoadingStatus.SUCCESS) {
                holder.landingChart.updateData(paymentsList.aggregatedPaymentList)
            } else if (paymentsList.status == LoadingStatus.FAILURE) {
                holder.landingChart.updateDescriptionError()
            }
        } else if (holder is LoadingHolder) {
            holder.loading.show()
        }
    }

    abstract fun onLoanClicked(loan: Loan)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_CHART -> return ChartHolder(LayoutInflater.from(parent!!.context).inflate(com.koshkin.loancaluclator.loancalculator.R.layout.adapter_landing_view_chart, parent, false))
            VIEW_TYPE_LOADING -> return LoadingHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_loading_layout, parent, false))
            VIEW_TYPE_ERROR -> return ErrorHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_error_layout, parent, false))
            else -> return LoanHolder(LayoutInflater.from(parent!!.context).inflate(com.koshkin.loancaluclator.loancalculator.R.layout.adapter_landing_view_loan, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return VIEW_TYPE_CHART
            1 -> {
                if (loans.status == LoadingStatus.LOADING)
                    return VIEW_TYPE_LOADING
                else if (loans.status == LoadingStatus.FAILURE)
                    return VIEW_TYPE_ERROR else return VIEW_TYPE_LOAN
            }
            else -> return VIEW_TYPE_LOAN
        }
    }

    val VIEW_TYPE_CHART: Int = 0
    val VIEW_TYPE_LOAN: Int = 11
    val VIEW_TYPE_LOADING: Int = 1
    val VIEW_TYPE_ERROR: Int = 2

    override fun getItemCount(): Int {
        if (loans.status == LoadingStatus.LOADING)
            return 2 //chart + loading
        else if (loans.status == LoadingStatus.FAILURE)
            return 2 //chart + error

        return loans.loans.values.size + 1
    }

    class ChartHolder : RecyclerView.ViewHolder {

        var landingChart: LandingChartView

        internal constructor(itemView: View) : super(itemView) {
            landingChart = itemView.findViewById(R.id.landing_chart_view_layout) as LandingChartView
        }
    }

    class LoadingHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var loading = itemView!!.findViewById(R.id.loading_progress_bar) as ContentLoadingProgressBar
    }

    class LoanHolder : RecyclerView.ViewHolder {

        var loanAmount: TextView
        var loanName: TextView
        var loanProvider: TextView

        internal constructor(itemView: View) : super(itemView) {
            loanAmount = itemView.findViewById(R.id.landing_loan_amount) as TextView
            loanName = itemView.findViewById(R.id.landing_loan_name) as TextView
            loanProvider = itemView.findViewById(R.id.landing_loan_provider) as TextView
        }
    }

    class ErrorHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    fun notifyChartUpdated() {
        notifyItemChanged(0)
    }

    fun notifyLoansUpdated() {
        notifyDataSetChanged()
    }

}
