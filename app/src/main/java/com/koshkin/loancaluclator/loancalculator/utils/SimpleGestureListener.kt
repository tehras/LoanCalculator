package com.koshkin.loancaluclator.loancalculator.utils

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by tehras on 7/10/16.
 *
 * Left and Right Gesture Listener
 */
abstract class SimpleGestureListener : GestureDetector.OnGestureListener {
    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    private val SWIPE_THRESHOLD = 200
    private val SWIPE_VELOCITY_THRESHOLD = 100
    private val TAG = "SimpleGestureListener"

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        var result = false
        try {
            val diffX = e2.x - e1.x
            Log.d(TAG, "diffX - $diffX")
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight()
                } else {
                    onSwipeLeft()
                }
            }
            result = true

        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return result
    }

    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
}