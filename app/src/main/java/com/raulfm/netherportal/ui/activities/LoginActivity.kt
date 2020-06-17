package com.raulfm.netherportal.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.raulfm.netherportal.R
import com.raulfm.netherportal.api.controllers.LoginController
import com.raulfm.netherportal.model.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val defaultRememberMe: Boolean = false
    private val defaultUsername: String = ""
    private val defaultKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glide.with(this).asGif().load(R.raw.nether_animated).into(loginPortalAnimation)

        loginRememberMe.setOnCheckedChangeListener { _, state -> saveRememberMeState(state) }
        enterButton.setOnClickListener { submitLogin() }
        checkPreviousRememberMe()
    }

    private fun submitLogin() {
        val user = User(loginUsername.text.toString(), loginKey.text.toString())
        LoginController.login(user,
            onSuccess = {
                saveRememberMeState(loginRememberMe.isChecked)
                saveCredentials()
                val i = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            },
            onBadCredentials = {
                Toast.makeText(this.applicationContext, "Wrong username or key", Toast.LENGTH_SHORT)
                    .show()
            },
            onError = {
                Toast.makeText(
                    this.applicationContext,
                    "Server is not available :(",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun checkPreviousRememberMe() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val rememberMe =
            sharedPref.getBoolean(getString(R.string.saved_remember_me), defaultRememberMe)
        val username = sharedPref.getString(getString(R.string.saved_username), defaultUsername)
        val key = sharedPref.getString(getString(R.string.saved_key), defaultKey)

        if (rememberMe && username.isNullOrBlank().not() && key.isNullOrBlank().not()) {
            loginRememberMe.isChecked = true
            loginUsername.setText(username)
            loginKey.setText(key)
            submitLogin()
        }
    }

    private fun saveRememberMeState(state: Boolean) = getPreferences(Context.MODE_PRIVATE).edit()
        .putBoolean(getString(R.string.saved_remember_me), state)
        .apply()

    private fun saveCredentials() {
        if (loginRememberMe.isChecked) {
            getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(getString(R.string.saved_remember_me), loginRememberMe.isChecked)
                .putString(getString(R.string.saved_username), loginUsername.text.toString())
                .putString(getString(R.string.saved_key), loginKey.text.toString())
                .apply()
        } else {
            getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(getString(R.string.saved_remember_me), loginRememberMe.isChecked)
                .putString(getString(R.string.saved_username), "")
                .putString(getString(R.string.saved_key), "")
                .apply()
        }
    }
}
