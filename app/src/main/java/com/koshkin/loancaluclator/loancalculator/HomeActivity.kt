package com.koshkin.loancaluclator.loancalculator

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.koshkin.loancaluclator.loancalculator.R.*
import com.koshkin.loancaluclator.loancalculator.R.anim.abc_grow_fade_in_from_bottom
import com.koshkin.loancaluclator.loancalculator.fragments.LandingScreenFragment

class HomeActivity : AppCompatActivity() {

    private var appBar: AppBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home)
        val toolbar = findViewById(id.toolbar) as Toolbar?
        appBar = findViewById(id.app_bar) as AppBarLayout

        setSupportActionBar(toolbar)

        initNestedScrollView(findViewById(id.nested_scroll_view) as NestedScrollView)
        hideToolBarShadow()

        if (savedInstanceState == null) {
            //start default
            startFragment(LandingScreenFragment())
        }

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

    private fun startFragment(landingScreenFragment: LandingScreenFragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(abc_grow_fade_in_from_bottom, anim.abc_slide_out_top)
                .replace(id.fragment_container, landingScreenFragment)
                .addToBackStack(landingScreenFragment.javaClass.simpleName)
                .commit()
    }
}
