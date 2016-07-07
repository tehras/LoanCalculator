package com.koshkin.loancaluclator.loancalculator

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.koshkin.loancaluclator.loancalculator.R.*
import com.koshkin.loancaluclator.loancalculator.R.anim.abc_grow_fade_in_from_bottom
import com.koshkin.loancaluclator.loancalculator.fragments.LandingScreenFragment

class HomeActivity : AppCompatActivity() {

    val TAG: String = "HomeActivity"

    private var appBar: AppBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(id.toolbar) as Toolbar?
        appBar = findViewById(id.app_bar) as AppBarLayout

        setSupportActionBar(toolbar)

        initNestedScrollView(findViewById(id.nested_scroll_view) as NestedScrollView)
        hideToolBarShadow()

        if (savedInstanceState == null) {
            //start default
            startFragment(LandingScreenFragment(), true)
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "backStackCount ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.fragments == null || supportFragmentManager.fragments.size == 0) {
            finish()
            return
        }
        super.onBackPressed()
    }

    private fun initNestedScrollView(nestedScrollView: NestedScrollView) {
        nestedScrollView.setOnScrollChangeListener { nestedScrollView: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > 0)
                showToolBarShadow()
            else
                hideToolBarShadow()
        }
    }

    private fun showToolBarShadow() {
        appBar!!.elevation = baseContext.resources.getDimensionPixelSize(dimen.default_elevation).toFloat()
    }

    private fun hideToolBarShadow() {
        appBar!!.elevation = 0.toFloat()
    }

    private fun startFragment(landingScreenFragment: LandingScreenFragment, first: Boolean) {
        val tran = supportFragmentManager.beginTransaction()
                .setCustomAnimations(abc_grow_fade_in_from_bottom, anim.abc_slide_out_top)
                .addToBackStack(landingScreenFragment.javaClass.simpleName)

        if (!first)
            tran.replace(R.id.fragment_container, landingScreenFragment)
        else tran.add(R.id.fragment_container, landingScreenFragment)

        tran.commit()
    }
}
