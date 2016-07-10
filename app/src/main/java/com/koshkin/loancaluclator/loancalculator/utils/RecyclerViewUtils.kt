package com.koshkin.loancaluclator.loancalculator.utils

import android.content.res.Configuration
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by koshkin on 7/4/16
 * RecyclerView Utils.
 */

fun RecyclerView.defaultRecyclerView() {
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    this.setHasFixedSize(true)

    // use a linear layout manager
    this.layoutManager = this.defaultLayoutManager()
}

fun RecyclerView.defaultLayoutManager(): RecyclerView.LayoutManager {
    if (this.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        return LinearLayoutManager(this.context)
    else {
        val gridLayoutManager = GridLayoutManager(this.context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type = this@defaultLayoutManager.adapter.getItemViewType(position)
                if (type < 10)
                    return 2
                else
                    return 1
            }
        }

        return gridLayoutManager
    }
}

fun RecyclerView.setExtraBottomPadding(bottom: Int) {
    val decorator = viewItemDecorator
    decorator.bottom = bottom

    this.addItemDecoration(decorator)
}

object viewItemDecorator : RecyclerView.ItemDecoration() {

    var top: Int = 0
    var bottom: Int = 0

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val itemPosition = parent!!.getChildAdapterPosition(view)
        val itemCount = state!!.itemCount

        //first
        if (itemPosition == 0)
            outRect!!.top = top
        else if (itemPosition == itemCount - 1)
            outRect!!.bottom = bottom
    }
}

