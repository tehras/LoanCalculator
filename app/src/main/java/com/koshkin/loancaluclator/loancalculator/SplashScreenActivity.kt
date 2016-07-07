package com.koshkin.loancaluclator.loancalculator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    val FIREBASE_SIGN_IN: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()


        if (auth.currentUser != null) {
            // already signed in
            // start HomeActivity
            startLoggedInActivity()
        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.SplashTheme)
                            .setLogo(R.drawable.logo_googleg_color_18dp)
                            .setProviders(
                                    AuthUI.FACEBOOK_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER,
                                    AuthUI.EMAIL_PROVIDER)
                            .build(),
                    FIREBASE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(javaClass.simpleName, "resultCode - " + resultCode)
        if (requestCode == FIREBASE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startLoggedInActivity()
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    private fun startLoggedInActivity() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
