package com.raulfm.netherportal.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.raulfm.netherportal.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glide.with(this).asGif().load(R.raw.nether_animated).into(loginPortalAnimation);

        enterButton.setOnClickListener{ submitLogin() }
    }

    private fun submitLogin() {
        val i = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}
