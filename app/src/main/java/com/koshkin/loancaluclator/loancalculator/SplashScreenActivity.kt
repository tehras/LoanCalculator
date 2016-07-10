package com.koshkin.loancaluclator.loancalculator

//import com.firebase.ui.auth.AuthUI
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            // already signed in
            // start HomeActivity
            startLoggedInActivity(auth.currentUser)
        } else {
            // not signed in
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}

val TOKEN_KEY = "token_key_bundle"
