package com.android.mobiversa

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.mobiversa.MVP.Presenter.LoginPresenter
import com.android.mobiversa.MVP.PresenterImpl.LoginPresenterImpl
import com.android.mobiversa.MVP.View.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import java.text.DateFormat
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener, LoginView {


    private var isSignedUp = false
    private var loginPresenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.login_bg)
        loginPresenter = LoginPresenterImpl(this, this)

        //Click event to Login button
        login_btn.setOnClickListener(this)
        updateLoggedInState()

    }

    private fun updateLoggedInState() {
        //Retrieve shared prefs data
        val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val fileExists = preferences.getBoolean("isSignup", false)
        Log.e("Password", fileExists.toString())
        if (fileExists) {
            isSignedUp = true
            login_btn.text = getString(R.string.login)
        } else {
            login_btn.text = getString(R.string.signup)
        }
    }

    private fun checkUser(v: View) {
        loginPresenter?.login(login_user_name_edt.text.toString(), login_pswd_edt.text.toString(), isSignedUp)

    }

    override fun loginSuccess() {
        //Start next activity
        val hotelIntent = Intent(this, MainActivity::class.java)
        this.startActivity(hotelIntent)
    }

    override fun gettingError(s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingProgressBar() {

    }

    override fun hideLoadingProgressBar() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn -> {
                checkUser(v)
            }
        }
    }

    companion object {
        private const val PWD_KEY = "PWD"
    }
}
