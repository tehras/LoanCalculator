package com.koshkin.loancaluclator.loancalculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseUser

open class BaseActivity : AppCompatActivity() {

    fun startLoggedInActivity(currentUser: FirebaseUser?, func: () -> Unit) {
        if (currentUser != null) {
            currentUser.getToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    val token = it.result.token
                    startLoggedInActivity(token)
                    func()
                } else {
                    showError()
                }
            }
        } else {
            showError()
        }
    }

    fun startLoggedInActivity(currentUser: FirebaseUser?) {
        startLoggedInActivity(currentUser) {}
    }

    private val TAG = "SplashScreenActivity"

    fun startLoggedInActivity(token: String?) {
        Log.d(TAG, "starting activity with token - " + token)
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra(TOKEN_KEY, token)

        startActivity(intent)
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun showError() {
        //todo error
    }
}
