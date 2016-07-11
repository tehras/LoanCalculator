package com.koshkin.loancaluclator.loancalculator

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.koshkin.loancaluclator.loancalculator.R.dimen
import com.koshkin.loancaluclator.loancalculator.R.id
import com.koshkin.loancaluclator.loancalculator.fragments.LandingScreenFragment
import com.koshkin.loancaluclator.loancalculator.networking.userToken

class HomeActivity : BaseActivity() {

    val TAG: String = "HomeActivity"

    private var appBar: AppBarLayout? = null
    var fab: FloatingActionButton? = null
    var hideFab: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null) {
            val token = intent.getStringExtra(TOKEN_KEY)

            userToken = token
        }

        setContentView(R.layout.activity_home)
        val toolbar = findViewById(id.toolbar) as Toolbar?
        appBar = findViewById(id.app_bar) as AppBarLayout

        setSupportActionBar(toolbar)

        initNestedScrollView(findViewById(id.nested_scroll_view) as NestedScrollView)
        hideToolBarShadow()

        fab = findViewById(R.id.floating_action_button) as FloatingActionButton?

        if (savedInstanceState == null) {
            //start default
            LandingScreenFragment.create().startFragment(this, true)
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "backStackCount ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.fragments == null || supportFragmentManager.fragments.size <= 1) {
            finish()
            return
        }
        super.onBackPressed()
    }

    private fun initNestedScrollView(nestedScrollView: NestedScrollView) {
        nestedScrollView.setOnScrollChangeListener { nestedScrollView: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > 0) {
                showToolBarShadow()
            } else {
                hideToolBarShadow()
            }

            if (hideFab && fab != null && scrollY > 10) {
                if (scrollY == 0 || oldScrollY > (scrollY)) {
                    fab!!.show()
                } else {
                    fab!!.hide()
                }
            }
        }
    }

    private fun showToolBarShadow() {
        appBar!!.elevation = baseContext.resources.getDimensionPixelSize(dimen.default_elevation).toFloat()
    }

    private fun hideToolBarShadow() {
        appBar!!.elevation = 0.toFloat()
    }
}
