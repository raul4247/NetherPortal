package com.raulfm.netherportal.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raulfm.netherportal.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enterButton.setOnClickListener{ submitLogin() }
    }

    private fun submitLogin() {
        val i = Intent(this@LoginActivity, HomeActivity::class.java)
        finish()
        startActivity(i)
    }
}
