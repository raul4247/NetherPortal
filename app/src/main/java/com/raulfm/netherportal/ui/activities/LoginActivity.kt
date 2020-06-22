package com.raulfm.netherportal.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.raulfm.netherportal.R
import com.raulfm.netherportal.backend.api.controllers.LoginController
import com.raulfm.netherportal.extensions.setState
import com.raulfm.netherportal.model.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val defaultRememberMe: Boolean = false
    private val defaultUsername: String = ""
    private val defaultKey: String = ""

    private val autoLogKey = "autoLogKey"
    private var autoLog: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glide.with(this).asGif().load(R.raw.nether_animated).into(loginPortalAnimation)

        autoLog = intent.getBooleanExtra(autoLogKey, true)

        loginRememberMe.setOnCheckedChangeListener { _, state -> saveRememberMeState(state) }
        enterButton.setOnClickListener { submitLogin() }
        checkPreviousRememberMe()
    }

    private fun submitLogin() {
        enterButton.setState(false)
        setLoadingStatus(true)

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
                Toast.makeText(
                    this.applicationContext,
                    getString(R.string.wrong_credentails),
                    Toast.LENGTH_SHORT
                )
                    .show()
                enterButton.setState(true)
                setLoadingStatus(false)
            },
            onError = {
                Toast.makeText(
                    this.applicationContext,
                    getString(R.string.error_ocurred),
                    Toast.LENGTH_SHORT
                ).show()
                enterButton.setState(true)
                setLoadingStatus(false)
            }
        )
    }

    private fun setLoadingStatus(status: Boolean) {
        loginRememberMe.isEnabled = !status
        loginUsername.isEnabled = !status
        loginKey.isEnabled = !status

        if (status) {
            enterButton.background =
                ContextCompat.getDrawable(this, R.drawable.button_disabled_background)
            enterButton.text = " "
            enterButtonProgressBar.visibility = View.VISIBLE
        } else {
            enterButton.background =
                ContextCompat.getDrawable(this, R.drawable.button_primary_background)
            enterButton.text = getString(R.string.login_enter)
            enterButtonProgressBar.visibility = View.GONE
        }
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
            if (autoLog) submitLogin()
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
