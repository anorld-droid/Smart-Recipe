package com.example.smartrecipe

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartrecipe.databinding.ActivityMainBinding
import com.example.smartrecipe.ui.subscription.SubscriptionActivity
import com.google.android.material.navigation.NavigationView

open class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        if (notifyList()) {
            hideCategories(true)
        }else{
            hideCategories(false)
        }
        appBarConfiguration = AppBarConfiguration(
             setOf(
                R.id.nav_home, R.id.nav_categories, R.id.nav_subscription,
                R.id.nav_how_it_works, R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun notifyList(): Boolean {
        var subscribed = false;
        for (p in SubscriptionActivity.subcribeItemIDs) {
            if (getSubscribeItemValueFromPref(p)) {
                subscribed = getSubscribeItemValueFromPref(p)
                break
            }
        }
        return subscribed
    }
    private val preferenceObject: SharedPreferences
        get() = applicationContext.getSharedPreferences(SubscriptionActivity.PREF_FILE, 0)

    private fun getSubscribeItemValueFromPref(PURCHASE_KEY: String): Boolean {
        return preferenceObject.getBoolean(PURCHASE_KEY, false)
    }

    private fun hideCategories (visibility : Boolean){
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        var navMenu :  Menu = navigationView.menu
        navMenu.findItem(R.id.nav_categories).isVisible = visibility
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}