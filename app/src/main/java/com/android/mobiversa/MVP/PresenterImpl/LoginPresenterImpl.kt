package com.android.mobiversa.MVP.PresenterImpl

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.mobiversa.Encryption
import com.android.mobiversa.LoginActivity
import com.android.mobiversa.MVP.Presenter.LoginPresenter
import com.android.mobiversa.MVP.View.LoginView
import com.android.mobiversa.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.text.DateFormat
import java.util.*

class LoginPresenterImpl(var context: Context, private val loginView: LoginView) : LoginPresenter {
  private var isSignedUp:Boolean = false;
    override fun login(useName: String, password: String,signUp:Boolean) {
        var success = false
        this.isSignedUp = signUp;
        val userName = useName
        val password = password

        //Check if already signed up
        if (isSignedUp) {

            if (userName=="MobiVersa"){
                val lastLogin = lastLoggedIn(password,context)

                if (lastLogin != null) {
                    success = true
                    loginView.gettingError(lastLogin)

                } else {
                    loginView.gettingError("Please check your password and try again.")
                }
            }else{
                loginView.gettingError("Please check your username and try again.")
            }

        } else {
            when {
                password.isEmpty() -> loginView.gettingError("Please enter a password!")
                "MobiVersa" == useName-> {
                    //Save to shared prefs
                    val session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
                    session.putBoolean("isSignup", true)
                    session.apply()
                    success = true
                }
                else -> loginView.gettingError("Passwords do not match!")
            }
        }

        if (success) {
            saveLastLoggedInTime(password,context)
            loginView.loginSuccess()

        }


    }
    private fun lastLoggedIn(password: String,context: Context): String? {
        //Get password
        val password = CharArray(password.length)
        Log.e("Password", password.toString())
      //  login_pswd_edt.text?.getChars(0, password.length, password, 0)

        //Retrieve shared prefs data
        val preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val base64Encrypted = preferences.getString("l", "")
        val base64Salt = preferences.getString("lsalt", "")
        val base64Iv = preferences.getString("liv", "")

        //Base64 decode
        val encrypted = Base64.decode(base64Encrypted, Base64.NO_WRAP)
        val iv = Base64.decode(base64Iv, Base64.NO_WRAP)
        val salt = Base64.decode(base64Salt, Base64.NO_WRAP)

        //Decrypt
        val decrypted = Encryption().decrypt(
            hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted), password)

        var lastLoggedIn: String? = null
        decrypted?.let {
            lastLoggedIn = String(it, Charsets.UTF_8)
        }
        return lastLoggedIn
    }

    private fun saveLastLoggedInTime(password: String,context: Context) {
        //Get password
        val password = CharArray(password.length)
        Log.e("Password", password.toString())
       // login_pswd_edt.text?.getChars(0, login_pswd_edt.length(), password, 0)

        //Base64 the data
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        val map =
            Encryption().encrypt(currentDateTimeString.toByteArray(Charsets.UTF_8), password)
        val valueBase64String = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val saltBase64String = Base64.encodeToString(map["salt"], Base64.NO_WRAP)
        val ivBase64String = Base64.encodeToString(map["iv"], Base64.NO_WRAP)

        //Save to shared prefs
        val editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
        editor.putString("l", valueBase64String)
        editor.putString("lsalt", saltBase64String)
        editor.putString("liv", ivBase64String)
        editor.apply()
    }
    override fun logOut() {

    }
}
