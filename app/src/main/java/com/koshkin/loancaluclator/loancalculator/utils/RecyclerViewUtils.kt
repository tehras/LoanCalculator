package com.koshkin.loancaluclator.loancalculator.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by koshkin on 7/4/16
 * RecyclerView Utils.
 */

fun RecyclerView.defaultRecyclerView() {
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    this.setHasFixedSize(true)

    // use a linear layout manager
    this.layoutManager = LinearLayoutManager(this.context)
}

