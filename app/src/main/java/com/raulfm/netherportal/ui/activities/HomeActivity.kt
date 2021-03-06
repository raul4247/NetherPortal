package com.raulfm.netherportal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.raulfm.netherportal.R
import com.raulfm.netherportal.backend.singletons.TokenManager
import com.raulfm.netherportal.ui.fragments.LogsFragment
import com.raulfm.netherportal.ui.fragments.ServerControlFragment
import com.raulfm.netherportal.ui.fragments.WaypointsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val serverControlFragment = ServerControlFragment.newInstance()
    private val waypointsFragment = WaypointsFragment.newInstance()
    private val logsFragment = LogsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeToolbar.title = ""
        setSupportActionBar(homeToolbar)

        homeBottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId in listOf(
                    R.id.navigation_server_control,
                    R.id.navigation_waypoints,
                    R.id.navigation_logs
                )
            ) {
                when (item.itemId) {
                    R.id.navigation_server_control ->
                        openFragment(serverControlFragment)
                    R.id.navigation_waypoints ->
                        openFragment(waypointsFragment)
                    R.id.navigation_logs ->
                        openFragment(logsFragment)
                }
                homeToolbarTitle.text = item.title
                return@OnNavigationItemSelectedListener true
            } else
                return@OnNavigationItemSelectedListener false
        })
        homeBottomNavigationView.selectedItemId = R.id.navigation_server_control

        logoutButton.setOnClickListener { logout(false) }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFragmentContainer, fragment)
        transaction.commit()
    }

    fun logout(autoLog: Boolean) {
        val i = Intent(this@HomeActivity, LoginActivity::class.java)
        i.putExtra("autoLogKey", autoLog)
        startActivity(i)
        finish()
    }
}
