package com.koshkin.loancaluclator.loancalculator.fragments.loans

import com.koshkin.loancaluclator.loancalculator.BaseFragment
import com.koshkin.loancaluclator.loancalculator.utils.putAttribute

/**
 * Created by tehras on 7/10/16.
 *
 * Edit Loan View
 */
class EditLoanFragment : BaseFragment() {

    companion object Factory {
        val BUNDLE_LOAN_KEY = "bundle_loan_key"
        fun create(loanId: String): EditLoanFragment = EditLoanFragment().putAttribute { putString(BUNDLE_LOAN_KEY, loanId) } as EditLoanFragment
    }
}
