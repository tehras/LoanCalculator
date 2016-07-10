package com.koshkin.loancaluclator.loancalculator

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BaseActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private val TAG = "LoginActivity"
    private val RC_SIGN_IN = 9001


    override fun onConnectionFailed(result: ConnectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + result)
        Toast.makeText(this, "Google Play Services Error.", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login_google_button -> signIn()
        }
    }

    var googleApiClient: GoogleApiClient? = null
    var auth: FirebaseAuth? = null
    var authListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (findViewById(R.id.login_google_button) as View).setOnClickListener(this)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // [END config_signin]
        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

        // [START initialize_auth]
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        // [START auth_state_listener]
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
            // [START_EXCLUDE]

            // [END_EXCLUDE]
        }
        // [END auth_state_listener]
    }

    override fun onStart() {
        super.onStart()
        if (auth != null && authListener != null)
            auth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null && auth != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

    /**
     * Send back success
     */
    @SuppressWarnings("unused")
    fun sendBackSuccessUser() {
        val auth = FirebaseAuth.getInstance()

        startLoggedInActivity(auth.currentUser){hideProgressDialog()}
    }

    /**
     * Send back failed
     */
    fun sendBackFailedUse() {
        setResult(Activity.RESULT_CANCELED, Intent())
    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                if (account != null)
                    firebaseAuthWithGoogle(account)
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                sendBackFailedUse()
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        if (auth == null)
            return

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId())
        // [START_EXCLUDE silent]
        showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null)
        auth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful())

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful) {
                hideProgressDialog()
                Log.w(TAG, "signInWithCredential", task.exception)
                Toast.makeText(this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            } else {
                sendBackSuccessUser()
            }
        }
    }

    private var dialog: ProgressDialog? = null

    private fun showProgressDialog() {
        dialog = ProgressDialog.show(this@LoginActivity, "",
                "Logging in, please wait...", true)
    }

    private fun hideProgressDialog() {
        if (dialog != null)
            dialog!!.dismiss()
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

}
