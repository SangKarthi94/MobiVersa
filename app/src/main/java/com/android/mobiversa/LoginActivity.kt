package com.android.mobiversa

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.text.DateFormat
import java.util.*

class LoginActivity : AppCompatActivity() , View.OnClickListener{

    private var isSignedUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.login_bg)

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

        var success = false
        val userName = login_user_name_edt.text.toString()
        val password = login_pswd_edt.text.toString()

        //Check if already signed up
        if (isSignedUp) {

            if (userName=="MobiVersa"){
                val lastLogin = lastLoggedIn()

                if (lastLogin != null) {
                    success = true
                    Toast.makeText(applicationContext,"Last login: $lastLogin",Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(applicationContext,"Please check your password and try again.",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"Please check your username and try again.",Toast.LENGTH_LONG).show()
            }

        } else {
            when {
                password.isEmpty() -> Toast.makeText(applicationContext,"Please enter a password!",Toast.LENGTH_SHORT).show()
                "MobiVersa" == login_user_name_edt.text.toString() -> {
                    //Save to shared prefs
                    val session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
                    session.putBoolean("isSignup", true)
                    session.apply()
                    success = true
                }
                else -> Toast.makeText(applicationContext,"Passwords do not match!",Toast.LENGTH_SHORT).show()
            }
        }

        if (success) {

            saveLastLoggedInTime()

            //Start next activity
            val context = v.context
            val hotelIntent = Intent(context, MainActivity::class.java)
            context.startActivity(hotelIntent)
        }



    }
    private fun lastLoggedIn(): String? {
        //Get password
        val password = CharArray(login_pswd_edt.length())
        Log.e("Password", password.toString())
        login_pswd_edt.text?.getChars(0, login_pswd_edt.length(), password, 0)

        //Retrieve shared prefs data
        val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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

    private fun saveLastLoggedInTime() {
        //Get password
        val password = CharArray(login_pswd_edt.length())
        Log.e("Password", password.toString())
        login_pswd_edt.text?.getChars(0, login_pswd_edt.length(), password, 0)

        //Base64 the data
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        val map =
            Encryption().encrypt(currentDateTimeString.toByteArray(Charsets.UTF_8), password)
        val valueBase64String = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val saltBase64String = Base64.encodeToString(map["salt"], Base64.NO_WRAP)
        val ivBase64String = Base64.encodeToString(map["iv"], Base64.NO_WRAP)

        //Save to shared prefs
        val editor = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
        editor.putString("l", valueBase64String)
        editor.putString("lsalt", saltBase64String)
        editor.putString("liv", ivBase64String)
        editor.apply()
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_btn -> {
               checkUser(v)
            }
        }
    }

    companion object {
        private const val PWD_KEY = "PWD"
    }
}
